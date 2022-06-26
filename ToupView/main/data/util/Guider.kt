package com.example.astroclient.util

import android.graphics.PointF
import android.graphics.Rect
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.example.astroclient.TpConst
import com.example.astroclient.TpLib
import com.example.astroclient.model.Chart
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

interface CallbackListener {
    fun setMessage(message:String)
}

fun Closeable.closeNull(): Closeable? {
    try {
        close()
    } catch (e: IOException) {
    }
    return null
}

private fun except(t: Throwable?): String {
    if (t == null) return ""
    val baos = ByteArrayOutputStream()
    try {
        t.printStackTrace(PrintStream(baos))
    } finally {
        baos.closeNull()
    }
    return baos.toString()
}


fun parseString(vararg objs: Any): String {
    val sb = StringBuilder()
    for (mes in objs) {
        if (mes is String) {
            sb.append(mes.toString())
        } else if (mes is Array<*>) {
            sb.append(Arrays.toString(mes))
        } else if (mes is Throwable) {
            sb.append(except(mes))
        } else if (mes is List<*>) {
            sb.append("[")
            for (o in mes) {
                sb.append(o.toString())
                sb.append(",")
            }
            sb.insert(sb.length - 1, "]")
        } else if (mes is Map<*, *>) {
            val map = mes
            val keys = map.keys
            sb.append("{")
            for (key in keys) {
                sb.append(key)
                sb.append(":")
                sb.append(map[key])
                sb.append(",")
            }
            sb.insert(sb.length - 1, "}")
        } else {
            sb.append(mes.toString())
        }
        sb.append(" ")
    }
    sb.deleteCharAt(sb.length - 1)
    return sb.toString()
}


suspend fun Guider.WaitForSettleDone() {
    while (true) {
        var s = checkSetting()
        if (s.Done) {
            if (!TextUtils.isEmpty(s.Error))
                throw GuiderException(s.Error)
            loge("settling is done")
            break
        }
        logi("settling dist ${s.Distance}/${s.SettlePx} time ${s.Time}/${s.SettleTime}")
        Thread.sleep((1 * 1000).toLong())
    }
}

data class Accum(var n: Int = 0, var a: Double = 0.0, var q: Double = 0.0, var peek: Double = 0.0) {
    fun reset() {
        n = 0
        a = 0.0
        q = 0.0
        peek = 0.0
    }

    fun add(x: Double) {
        var ax = abs(x)
        if (ax > peek)
            peek = ax

        n += 1
        var d = x - a
        a += d / n
        q += (x - a) * d
    }

    fun mean() = a

    fun stdev(): Double {
        if (n < 1)
            return 0.0
        return Math.sqrt((q / n))
    }
}

data class SettleProgress(
    var Done: Boolean = false,
    var Distance: Double = 0.0,
    var SettlePx: Double = 0.0,
    var Time: Double = 0.0,
    var SettleTime: Double = 0.0,
    var Status: Int = 0,
    var Error: String = ""
)

data class GuideStats(
    var rms_tot: Double = 0.0,
    var rms_ra: Double = 0.0,
    var rms_dec: Double = 0.0,
    var peak_ra: Double = 0.0,
    var peak_dec: Double = 0.0
) {
    fun hypot() {
        rms_tot = Math.hypot(rms_ra, rms_dec)
    }
}

enum class CalibrationType {
    MOUNT, AO, BOTH
}

data class Profile(val id: Int, val name: String, var selected: Boolean = false)

//data class DeviceDrv(val name: String)

class GuiderException(st: String) : Exception(st)

class SocketIO {
    private var sock: Socket? = null
    private lateinit var bufReader: BufferedReader
    private lateinit var outStream: OutputStream
    var isTerminate = false
        set(value) {
            if (value) {
                sock?.run {
                    logi(TAG, "shutdown")
                    shutdownInput()
                    shutdownOutput()
                }
            }
        }
    var isConnected = false
        private set
        get() = sock != null

    companion object {
        private const val TAG = "SocketIO"
    }


    @Throws(IllegalStateException::class)
    fun connect(host: String, port: Int) {
        if (isConnected) {
            throw IllegalStateException("Already connected!")
        }

        sock = Socket(host, port)
        sock?.run {
            bufReader = BufferedReader(InputStreamReader(inputStream))
            outStream = outputStream
        }
        isConnected = true
    }

    fun disconnect() {
        if (!isConnected) {
            return
        }

        bufReader.closeNull()
        outStream.closeNull()
        sock = sock?.run {
            closeNull()
        } as Socket?

        isConnected = false
    }

    @Throws(IllegalStateException::class)
    fun writeLine(s: String) {
        if (!isConnected) {
            throw IllegalStateException("Is not connected!")
        }

        try {
            outStream.write("$s\r\n".toByteArray(Charset.forName("utf-8")))
            outStream.flush()
        } catch (e: Exception) {
        }
    }

    @Throws(IllegalStateException::class)
    fun readLine(): String {
        if (!isConnected) {
            throw IllegalStateException("Is not connected!")
        }

        var line = ""
        try {
            line = bufReader.readLine()//工作在阻塞模式，舍弃循环
        } catch (e: Exception) {
            line = ""
        }

        return line
    }
}

class Guider(var host: String) {
    companion object {
        private const val TAG = "Guider"
        private const val DEFAULT_STOPCAPTURE_TIMEOUT = 10

        private fun accumGetStats(ra: Accum, dec: Accum): GuideStats {
            val stats = GuideStats()
            stats.rms_ra = ra.stdev()
            stats.rms_dec = dec.stdev()
            stats.peak_ra = ra.peek
            stats.peak_dec = dec.peek
            return stats
        }

        private fun isFailed(resp: JSONObject): Boolean {
            return resp.has("error")
        }
    }


    private var previewHandler: Handler? = null
    fun setPreviewHandler(handler: Handler?) {
        previewHandler = handler
    }

    var devicesCurrentValid = ArrayList<String>()
    var chartData = Chart()
    var portOffset = 0
    private var id = 0
    private val condition = Object()
    private val lock = Any()
    private val responseQueue: LinkedList<JSONObject> = LinkedList<JSONObject>()
    private var io = SocketIO()
    private var worker: Thread? = null
    private var isTerminate
        set(value) {
            io.isTerminate = value
        }
        get() = io.isTerminate

    var isConnected = false
        private set
        get() = io.isConnected

    var isGuiding = false
        private set
        get() {
            val value = AppState
            return value.equals("Guiding") or value.equals("LostLock")
        }

    private val _worker = object : Runnable {
        override fun run() {
            while (!isTerminate) {
                val readLine = io.readLine()
                if ("".equals(readLine) && !isTerminate) {
                    //server disconnected!
                    //todo
                    break
                }

                try {
                    val jsonObject = JSONObject(readLine)
                    logi(TAG, "测试数据" + jsonObject.toString())//todo
                    if (jsonObject.has("jsonrpc")) {
                        //response
                        synchronized(condition) {
                            responseQueue.add(jsonObject)
                            condition.notify()
                        }
                    } else {
                        //event
                        handleEvent(jsonObject)
                    }

                } catch (e: Exception) {

                }
            }
        }

    }

    var AppState: String = ""
        private set(value) = synchronized(lock) {
            field = value
        }
        get() = synchronized(lock) { field }
    var AvgDist = 0
        private set(value) = synchronized(lock) {
            field = value
        }
        get() = synchronized(lock) { field }

    private var accum_active: AtomicBoolean = AtomicBoolean(false)
    private var settle_px = 0.0
    private var accum_ra = Accum()
    private var accum_dec = Accum()
    var Stats = GuideStats()
        private set(value) = synchronized(lock) {
            field = value
        }
        get() {
            checkConnected()
            return synchronized(lock) {
                val copy = field.copy()
                copy.hypot()
                copy
            }
        }

    private var Settle: SettleProgress? = null
        set(value) = synchronized(lock) {
            field = value
        }

    private fun handleEvent(ev: JSONObject) {
        when (ev["Event"]) {
            "AppState" -> {
                val state = ev["State"].toString()
                AppState = state
                if (isGuiding) {
                    AvgDist = 0
                }
            }
            "Version" -> {

            }
            "DeviceList" -> {

            }
            "DeviceChanged" -> {
                //Parse Current Devices
                devicesCurrentValid.clear()
                try {
                    var list = ev.getJSONArray("DeviceArray")
                    for (i in 0 until list.length()) {
                        var p = list.getJSONObject(i)
                        devicesCurrentValid.add(p.getString("Name"))
                    }

                    previewHandler?.apply {
                        val Msg = obtainMessage()
                        Msg.what = TpConst.MSG_DEVICE_CHANGED
                        sendMessage(Msg)
                    }
                } catch (exp: JSONException) {
                    exp.printStackTrace()
                }
            }
            "StartGuiding" -> {
                accum_active.set(true)
                accum_ra.reset()
                accum_dec.reset()
                Stats = accumGetStats(accum_ra, accum_dec)
            }
            "GuideStep" -> {
                if (accum_active.get()) {
                    accum_ra.add(ev.getDouble("RADistanceRaw"))
                    accum_dec.add(ev.getDouble("DECDistanceRaw"))
                    Stats = accumGetStats(accum_ra, accum_dec)
                }

                AppState = "Guiding"
                AvgDist = ev.getInt("AvgDist")

                previewHandler?.apply {
                    handleChartData(ev)
                    val Msg = obtainMessage()
                    Msg.what = TpConst.MSG_CHART_UPDATE
                    sendMessage(Msg)
                }
            }
            "SettleBegin" -> {
                accum_active.set(false)  // exclude GuideStep messages from stats while settling
            }

            "Settling" -> {
                val s = SettleProgress()
                s.Distance = ev.getDouble("Distance")
                s.SettlePx = settle_px
                s.Time = ev.getDouble("Time")
                s.SettleTime = ev.getDouble("SettleTime")
                Settle = s
            }
            "SettleDone" -> {
                accum_active.set(true)
                accum_ra.reset()
                accum_dec.reset()
                val stats = accumGetStats(accum_ra, accum_dec)
                val s = SettleProgress()
                s.Done = true
                s.Status = ev.getInt("Status")
                s.Error = ev.getString("Error")

                Settle = s
                Stats = stats
            }
            "Paused" -> {
                AppState = "Paused"
            }
            "StartCalibration" -> {
                AppState = "Calibrating"
            }
            "LoopingExposures" -> {
                AppState = "Looping"
            }
            "LoopingExposuresStopped", "GuidingStopped" -> {
                AppState = "Stopped"
            }
            "StarLost" -> {
                AppState = "LostLock"
                AvgDist = ev.getInt("AvgDist")
            }
            "StarSelected" -> {
                //handle start selected to draw a rect
            }
        }
    }

    fun handleChartData(ev: JSONObject) {
        try {
            chartData.dx = null
            chartData.dx = ev.getDouble("dx")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.dy = null
            chartData.dy = ev.getDouble("dy")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.RADistanceRaw = null
            chartData.RADistanceRaw = ev.getDouble("RADistanceRaw")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.DecDistanceRaw = null
            chartData.DecDistanceRaw = ev.getDouble("DECDistanceRaw")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.RADistanceGuide = null
            chartData.RADistanceGuide = ev.getDouble("RADistanceGuide")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.DecDistanceGuide = null
            chartData.DecDistanceGuide = ev.getDouble("DECDistanceGuide")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.RADuration = null
            chartData.RADuration = ev.getInt("RADuration")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.RADirection = null
            chartData.RADirection = ev.getString("RADirection")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.DECDuration = null
            chartData.DECDuration = ev.getInt("DECDuration")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.DECDirection = null
            chartData.DECDirection = ev.getString("DECDirection")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.StarMass = null
            chartData.StarMass = ev.getDouble("StarMass")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }
        try {
            chartData.SNR = null
            chartData.SNR = ev.getDouble("SNR")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.HFD = null
            chartData.HFD = ev.getDouble("HFD")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }

        try {
            chartData.AvgDist = null
            chartData.AvgDist = ev.getDouble("AvgDist")
        } catch (exp: JSONException) {
            exp.printStackTrace()
        }
    }

    @Throws(IllegalStateException::class)
    fun connect() {
        if (isConnected) {
            throw IllegalStateException("Already connected! $host:${4400 + portOffset}")
        }
        logi(TAG, "try to connect " + host)
        io.connect(host, 4400 + portOffset)
        isTerminate = false
        worker = Thread(_worker)
        worker?.start()
        loge(TAG, "connect!")
    }

    @Throws(IllegalStateException::class)
    fun disconnect() {
        if (!isConnected) {
            throw IllegalStateException("Already disconnected!")
        }

        isTerminate = true
        worker?.run {
            if (isAlive) {
                join()
            }
        }
        io.disconnect()
        id = 0

        AvgDist = 0
        loge(TAG, "disconnect")
    }

    fun setHostAddr(hostAddr: String) {
        host = hostAddr
    }

    private fun <T> makeJsonRpc(method: String, params: T?): String {
        val req = JSONObject()
        req.put("method", method)
        req.put("id", id++)

        params?.run {
            if ((this is JSONObject) or (this is JSONArray)) {
                req.put("params", this)
            } else {
                val jsonArray = JSONArray()
                jsonArray.put(this)
                req.put("params", jsonArray)
            }
        }

        logi(TAG, req)
        return req.toString()
    }

    @Throws(IllegalStateException::class)
    private fun checkConnected() {
        if (!isConnected)
            throw IllegalStateException("Server disconnected!")
    }
    var callbackListener: CallbackListener? = null

    fun setOnListener(callback: CallbackListener){
        callbackListener = callback
    }



    /***
     * param带多个格式的时候，以jsonObject带name的形式包装
     * 例如：
     * {"method": "dither", "params": {"amount": 10, "raOnly": false, "settle": {"pixels": 1.5, "time": 8, "timeout": 30}}, "id": 42}
     * 注意：
     *  对于optional形式的参数，如果没有传递数据，建议使用第二种方案
     */
    @Throws(IllegalStateException::class)
    suspend fun <T> call(method: String, params: T?): JSONObject {
        val makeJsonRpc = makeJsonRpc(method, params)
        callbackListener?.setMessage(makeJsonRpc.toString())
        io.writeLine(makeJsonRpc)
        synchronized(condition) {
            while (responseQueue.size <= 0) {
                condition.wait()
            }
        }
        val resp = responseQueue.pop()
        callbackListener?.setMessage(resp.toString())
        if (isFailed(resp))
            throw GuiderException(resp["error"].toString())
        return resp
    }

    suspend fun captureSingleFrame(exposure: Int?, subframe: Rect?) {
        var jsonObject: JSONObject? = null
        if (exposure != null || subframe != null) {
            jsonObject = JSONObject()
            exposure?.run {
                jsonObject.put("exposure", exposure)
            }

            subframe?.run {
                val jsonArray = JSONArray()
                jsonArray.put(left)
                jsonArray.put(top)
                jsonArray.put(width())
                jsonArray.put(height())
                jsonObject.put("subframe", jsonArray)
            }
        }
        call("capture_single_frame", jsonObject?.toString())
    }

    suspend fun select_camera(device: String?, name: String?) {
        var jsonObject: JSONObject? = null
        if (device != null || name != null) {
            jsonObject = JSONObject()
            device?.run {
                jsonObject.put("device", device)
            }

            name?.run {
                jsonObject.put("name", name)
            }
        }
        call("select_camera", jsonObject)
    }

    suspend fun clearCalibration(which: CalibrationType) {
        when (which) {
            CalibrationType.MOUNT -> {
                call("clear_calibration", "mount")
            }
            CalibrationType.BOTH -> {

                call("clear_calibration", "both")
            }
            CalibrationType.AO -> {
                call("clear_calibration", "ao")
            }
        }
    }

    @Throws(IllegalStateException::class, GuiderException::class)
    suspend fun set_dither(
        ditherPixels: Double,
        settlePixels: Double,
        settleTime: Double,
        settleTimeout: Double
    ) {
        checkConnected()
        val s = SettleProgress()
        s.Distance = ditherPixels
        s.SettlePx = settlePixels
        s.SettleTime = settleTime

        synchronized(lock) {
            Settle?.run {
                if (!Done)
                    throw GuiderException("cannot guide while settling")
            }

            Settle = s
        }

        try {
            val params = JSONArray()
            val settleObj = JSONObject()
            settleObj.put("pixels", settlePixels)
            settleObj.put("time", settleTime)
            settleObj.put("timeout", settleTimeout)

            params.put(ditherPixels)
            params.put(false)
            params.put(settleObj)

            call("set_dither", params)
        } catch (e: Exception) {
            Settle = null
            throw e
        }
    }

    suspend fun findStar(): DoubleArray {
        val resp = call("auto_find_star", null)
        val doubleArray = DoubleArray(2)
        try {
            var lockPosition = resp.getJSONArray("result")//TODO maybe crash
            doubleArray[0] = lockPosition.getDouble(0)
            doubleArray[1] = lockPosition.getDouble(1)
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            val code = resp.getInt("code")
            val message = resp.getString("message")
            val strerror = resp.getString("error")
            Log.e(
                TAG,
                "findStar: code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return doubleArray
    }

    suspend fun findStarByPos(pt: PointF): DoubleArray {
        val ptArry = JSONArray()
        pt.run {
            ptArry.put(x)
            ptArry.put(y)
        }
        val param = JSONObject()
        param.put("pos", ptArry)
        val resp = call("auto_find_star_by_pos", param)
        val doubleArray = DoubleArray(2)
        try {
            val lockPosition = resp.getJSONArray("result") // TODO maybe crash
            doubleArray[0] = lockPosition.getDouble(0)
            doubleArray[1] = lockPosition.getDouble(1)
            Log.i(TAG, "findStarByPos: x = " + doubleArray[0] + ", y = " + doubleArray[1])
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            val code = resp.getInt("code")
            val message = resp.getString("message")
            val strerror = resp.getString("error")
            Log.e(
                TAG,
                "findStarByPos: code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return doubleArray
    }

    suspend fun setExposure(newExp: Int): Boolean {
        if (!isConnected)
            return false;
        val param = JSONObject()
        param.put("exposure", newExp)
        val resp = call("set_exposure", param)
        var retcode = false
        try {
            retcode = resp.getBoolean("result")
            if (retcode)
                logi(TAG, "setExposure success!")
        } catch (exp: JSONException) {
            Log.i(TAG, "getInt failed!")
            val code = resp.getInt("code")
            val message = resp.getString("message")
            val strerror = resp.getString("error")
            Log.e(
                TAG,
                "setExposure: code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return retcode
    }

    suspend fun getCameraFrameSize(): Array<Int> {
        val resp = call("get_camera_frame_size", null)
        val jsonArray = resp.getJSONArray("result")
        return arrayOf(
            jsonArray.getInt(0),
            jsonArray.getInt(1)
        )
    }

    @Throws(IllegalStateException::class, GuiderException::class)
    suspend fun guide(settlePixels: Double, settleTime: Double, settleTimeout: Double) {
        checkConnected()
        val s = SettleProgress()
        s.SettlePx = settlePixels
        s.SettleTime = settleTime

        synchronized(lock) {
            Settle?.run {
                if (!Done)
                    throw GuiderException("cannot guide while settling")
            }

            Settle = s
        }

        try {
            val jsonArray = JSONArray()
            val jsonSettle = JSONObject()
            jsonSettle.put("pixels", settlePixels)
            jsonSettle.put("time", settleTime)
            jsonSettle.put("timeout", settleTimeout)

            jsonArray.put(jsonSettle)
            jsonArray.put(false)
            call("guide", jsonArray)

            settle_px = settlePixels
        } catch (e: Exception) {
            Settle = null
            throw e
        }
    }

    suspend fun isSetting(): Boolean {
        checkConnected()
        synchronized(lock) {
            if (Settle != null)
                return true
        }
        val res = call("get_settling", null)
        val ret = res.getBoolean("result")
        if (ret) {
            val s = SettleProgress()
            s.Distance = -1.0
            Settle = s
        }
        return ret
    }

    @Throws(GuiderException::class)
    suspend fun checkSetting(): SettleProgress {
        checkConnected()

        return synchronized(lock) {
            Settle?.run {
                val s = copy()
                if (Done) {
                    Settle = null
                }
                s
            } ?: throw GuiderException("not settling")
        }
    }

    //

    /**
     * stop looping and guiding
     */
    @Throws(IllegalStateException::class, GuiderException::class)
    suspend fun stopCapture(timeoutSeconds: Int = 10) {
        call("stop_capture", null)
        for (i in 0 until timeoutSeconds) {
            if (AppState == "Stopped")
                return
            Thread.sleep((1 * 1000).toLong())
            checkConnected()
        }
        val res = call("get_app_state", null)
        val st = res.getString("result")
        AppState = st
        if (st == "Stopped")
            return
        throw GuiderException("guider did not stop capture after ${timeoutSeconds} seconds!")
    }

    /**
     * start looping exposures
     */

    suspend fun loop(timeoutSeconds: Int = 10) {
        checkConnected()
        if (AppState == "Looping") {
            return
        }
        val res = call("get_exposure", null)
        val exp = res.getLong("result")
        call("start_loop", null)
        Thread.sleep(exp * 1000)
        for (i in 0 until timeoutSeconds) {
            if (AppState == "Looping")
                return
            Thread.sleep((1 * 1000).toLong())
            checkConnected()
        }
        throw  GuiderException("timed-out waiting for guiding to start looping")
    }

    suspend fun pixelScale(): Int {
        var res = call("get_pixel_scale", null)
        return res.getInt("result")
    }

    suspend fun startDriver(strDriver: String): Boolean {
        var param = JSONObject()
        param.put("driver", strDriver)
        var resp = call("start_single_driver", param)
        logi("测试数据", resp.toString())
        var retcode = false
        try {
            retcode = resp.getBoolean("result")
            if (retcode)
                logi(TAG, "start_single_driver success!")
        } catch (exp: JSONException) {
            Log.i(TAG, "start_single_driver failed!")
            var code = resp.getInt("code")
            var message = resp.getString("message")
            var strerror = resp.getString("error")
            Log.e(
                TAG,
                "code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return retcode
    }

    suspend fun stopDriver(strDriver: String): Boolean {
        var param = JSONObject()
        param.put("driver", strDriver)
        var resp = call("stop_single_driver", param)
        var retcode = false
        try {
            retcode = resp.getBoolean("result")
            if (retcode)
                logi(TAG, "stop_single_driver success!")
        } catch (exp: JSONException) {
            Log.i(TAG, "stop_single_driver failed!")
            var code = resp.getInt("code")
            var message = resp.getString("message")
            var strerror = resp.getString("error")
            Log.e(
                TAG,
                "code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return retcode
    }

    suspend fun shutdown():Boolean
    {
        val resp = call("shutdown", null)
        var retcode = false
        try {
            retcode = resp.getBoolean("result")
            if  (retcode)
                logi(TAG, "shutdown success!")
        } catch (exp: JSONException) {
            Log.i(TAG, "shutdown failed!")
        }
        return retcode
    }

    /**
     * get a list of the Equipment Profile names
     */
    suspend fun getEquipmentProfiles(): ArrayList<Profile> {
        var res = call("get_profiles", null)
        var profiles = ArrayList<Profile>()
        try {
            var list = res.getJSONArray("result")//TODO maybe crash
            for (i in 0 until list.length()) {
                var p = list.getJSONObject(i)
                var profile = Profile(p.getInt("id"), p.getString("name"))
                profile.selected = p.optBoolean("selected", false)
                profiles.add(profile)
            }
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            var code = res.getInt("code")
            var message = res.getString("message")
            var strerror = res.getString("error")
            Log.e(
                TAG,
                "code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return profiles
    }

    suspend fun getEquipmentCCDs(): ArrayList<String> {
        var res = call("get_devices", "CCDs")
        var devices = ArrayList<String>()
        try {
            var list = res.getJSONArray("result")   //TODO maybe crash
            for (i in 0 until list.length()) {
                var p = list.getJSONObject(i)
                devices.add(p.getString("device"))
            }
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            if (res.has("code")) {
                var code = res.getInt("code")
                var message = res.getString("message")
                var strerror = res.getString("error")
                Log.e(
                    TAG,
                    "code = " + code + ", message = " + message + ", error = " + strerror
                )
            }
        }
        return devices
    }

    suspend fun getEquipmentScopes(): ArrayList<String> {
        var res = call("get_devices", "Telescopes")
        var devices = ArrayList<String>()
        try {
            var list = res.getJSONArray("result")//TODO maybe crash
            for (i in 0 until list.length()) {
                var p = list.getJSONObject(i)
                devices.add(p.getString("device"))
            }
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            if (res.has("code")) {
                var code = res.getInt("code")
                var message = res.getString("message")
                var strerror = res.getString("error")
                Log.e(
                    TAG,
                    "code = " + code + ", message = " + message + ", error = " + strerror
                )
            }
        }
        return devices
    }

    suspend fun getEquipmentFWs(): ArrayList<String> {
        var res = call("get_devices", "Filter Wheels")
        var devices = ArrayList<String>()
        try {
            var list = res.getJSONArray("result")//TODO maybe crash
            for (i in 0 until list.length()) {
                var p = list.getJSONObject(i)
                devices.add(p.getString("device"))
            }
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            if (res.has("code")) {
                var code = res.getInt("code")
                var message = res.getString("message")
                var strerror = res.getString("error")
                Log.e(
                    TAG,
                    "code = " + code + ", message = " + message + ", error = " + strerror
                )
            }
        }
        return devices
    }

    suspend fun getSelectedEquipmentConnected(): Boolean {
        val res = call("get_connected", null)
        return res.optBoolean("result", false)
    }

    suspend fun connectEquipment() {
        if (!getSelectedEquipmentConnected()) {
            call("set_connected", true)
        }
    }

    suspend fun disconnectEquipment() {
        if (getSelectedEquipmentConnected()) {
            stopCapture(DEFAULT_STOPCAPTURE_TIMEOUT)
            call("set_connected", false)
        }
    }

    suspend fun setLockPosition(x: Float, y: Float, exact: Boolean = true) {
        val params = JSONArray()
        params.put(x)
        params.put(y)
        params.put(exact)
        call("set_lock_position", params);
    }

    suspend fun pause() {
        call("set_paused", true)
    }

    suspend fun unpause() {
        call("set_paused", false)
    }

    /**
     * delete profile by id
     *//*

    suspend fun deleteEquipmentProfiles(id: Int): Boolean {
        var res = call("delete_profile", id)
        var retcode = false
        try {
            retcode = res.getBoolean("result")
        } catch (exp: JSONException) {
            Log.i(TAG, "delete profile failed!")
            var code = res.getInt("code")
            var message = res.getString("message")
            var strerror = res.getString("error")
            Log.e(
                TAG,
                "code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return retcode
    }

    */
    /**
     * deselect_star
     *//*

    suspend fun deselectStar(): Boolean {
        var res = call("deselect_star", null)
        var retcode = false
        try {
            retcode = res.getBoolean("result")
        } catch (exp: JSONException) {
            Log.i(TAG, "deselect star failed!")
            var code = res.getInt("code")
            var message = res.getString("message")
            var strerror = res.getString("error")
            Log.e(
                TAG,
                "code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return retcode
    }

    suspend fun exportConfigSettings(): ArrayList<String> {
        var res = call("export_config_settings", null)
        var filenames = ArrayList<String>()
        try {
            var list = res.getJSONArray("result")   //TODO maybe crash
            for (i in 0 until list.length()) {
                var p = list.getJSONObject(i)
                filenames.add(p.getString("filename"))
            }
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            if (res.has("code"))
            {
                var code = res.getInt("code")
                var message = res.getString("message")
                var strerror = res.getString("error")
                Log.e(
                    TAG,
                    "code = " + code + ", message = " + message + ", error = " + strerror
                )
            }
        }
        return filenames
    }

    suspend fun flipCalibration(): Boolean{
        var res = call("flip_calibration", null)
        var retcode = false
        try {
            retcode = res.getBoolean("result")
        } catch (exp: JSONException) {
            Log.i(TAG, "flip calibration failed!")
            var code = res.getInt("code")
            var message = res.getString("message")
            var strerror = res.getString("error")
            Log.e(
                TAG,
                "code = " + code + ", message = " + message + ", error = " + strerror
            )
        }
        return retcode

    }


    enum class AxisType {
        RA,DEC,X,Y
    }

    suspend fun getAlgoParamNames(axis: AxisType): ArrayList<String>  {
        var res =  when (axis) {
            AxisType.RA -> {
                call("clear_calibration", "ra")
            }
            AxisType.DEC -> {
                call("clear_calibration", "dec")
            }
            AxisType.X -> {
                call("clear_calibration", "x")
            }
            AxisType.Y -> {
                call("clear_calibration", "y")
            }
        }

        var names = ArrayList<String>()
        try {
            var list = res.getJSONArray("result")   //TODO maybe crash
            for (i in 0 until list.length()) {
                var p = list.getJSONObject(i)
                names.add(p.toString())
            }
        } catch (exp: JSONException) {
            Log.i(TAG, "getJSONArray failed!")
            if (res.has("code"))
            {
                var code = res.getInt("code")
                var message = res.getString("message")
                var strerror = res.getString("error")
                Log.e(
                    TAG,
                    "code = " + code + ", message = " + message + ", error = " + strerror
                )
            }
        }

        return names
    }
*/

}