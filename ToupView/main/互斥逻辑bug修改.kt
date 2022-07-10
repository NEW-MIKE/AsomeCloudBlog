package com.example.astroclient
/*new*/
import android.content.Context
import android.print.PrintAttributes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.astroclient.databinding.SettingConnectBinding
import com.example.astroclient.databinding.SettingoConnectBinding
import com.example.astroclient.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectPopwindow(val context: Context) : PopupWindow() {
    private val TAG: String = "ConnectPopwindow"
    val inflatel: SettingConnectBinding
    val inflate: SettingoConnectBinding
    private var mainDevice: String? = null
    private var guideDevice: String? = null
    private var mainRecoveryFlag = false
    private var guideRecoveryFlag = false
    private var streamDeviceFlag = false
    private var ccdsFlag = false
    private var mainIsConnected = false
    private var guideIsConnected = false
    private var mainAdapterList =
        ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var guideAdapterList =
        ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var mainResolAdapterList =
        ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var guideResolAdapterList =
        ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)

    init {
        inflatel = SettingConnectBinding.inflate(LayoutInflater.from(context))
        inflate = inflatel.popuwindown
        contentView = inflatel.root
        isFocusable = true
        init()
        mainDevice()
        guideDevice()
        setPanel()
        pullStream()
    }

    var opened: Boolean? = false

    var hostAddr = ServerConnect.hostAddr
    var guider: Guider = ServerConnect.guider

    var profile: Profile? = null
    var choosenCCD: String? = null
    private var adapter = MySpinnerAdapter(context)
    private var adapterCCDs = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var adapterScope = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var adapterFW = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)

    fun init() {
        inflate.edittextHost.addTextChangedListener(afterTextChanged = {
            guider.setHostAddr(it.toString())
            hostAddr = it.toString();
        })

        disableView()
        inflate.connect.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                try {
                    if (isCheck) {
                        guider.connect()
                        withContext(Dispatchers.Main) {
                            inflate.selectCCDs.isEnabled = true
                            inflate.selectedCCDs.isEnabled = true
                            inflate.selectFW.isEnabled = true
                            inflate.selectedFW.isEnabled = true
                            inflate.selectScope.isEnabled = true
                            inflate.selectedScope.isEnabled = true

                            inflatel.camerasetting.apply {
                                cameramodechoose.isEnabled = true
                                connectall.isEnabled = true
                            }
                            if (!inflate.selectedCCDs.isChecked) {
                                inflatel.mainDevice.apply {
                                    connect.isEnabled = false
                                    select.isEnabled = false
                                }
                                inflatel.guidingDevice.apply {
                                    connect.isEnabled = false
                                    select.isEnabled = false
                                }
                            } else {
                                inflatel.mainDevice.apply {
                                    connect.isEnabled = true
                                    select.isEnabled = !connect.isChecked
                                }
                                inflatel.guidingDevice.apply {
                                    connect.isEnabled = true
                                    select.isEnabled = !connect.isChecked
                                }
                            }
                            loadEquipment()
                        }
                        (context as MainActivity).checkAppState()
                    } else {
                        guider.disconnect()

                        mainRecoveryFlag = true
                        guideRecoveryFlag = true
                        streamDeviceFlag = true
                        ccdsFlag = true
                        withContext(Dispatchers.Main) {
                            unloadEquipment()
                            disableView()
                        }
                        opened = false

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        unloadEquipment()
                        disableView()
                    }
                }
            }
        }


        inflate.selectedCCDs.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                if (isCheck) {
                    if (!ccdsFlag){
                        choosenCCD?.run {
                            guider.startDriver(choosenCCD!!)
                        }
                    }
                    ccdsFlag = false
                    withContext(Dispatchers.Main) {
                        inflatel.guidingDevice.apply {
                            connect.isEnabled = true
                            select.isEnabled = true
                        }
                        inflatel.mainDevice.apply {
                            connect.isEnabled = true
                            select.isEnabled = true
                        }
                    }
                } else {
                    choosenCCD?.run {
                        withContext(Dispatchers.Main) {
                            (context as MainActivity).stopStream()
                        }
                        try {

                            if (!ccdsFlag) {
                                guider.stopCapture()
                                guider.disconnectGuide()
                                guider.disconnectMain()
                                guider.stopDriver(choosenCCD!!)
                            }
                            ccdsFlag = false
                            withContext(Dispatchers.Main) {
                                inflatel.guidingDevice.apply {
                                    connect.isEnabled = false
                                    select.isEnabled = false
                                }
                                inflatel.mainDevice.apply {
                                    connect.isEnabled = false
                                    select.isEnabled = false
                                }
                                clearDeviceCache()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            }
        }

        inflate.selectCCDs.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    choosenCCD = adapterCCDs.getItem(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        inflate.selectedScope.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                if (isCheck) {
                } else {
                }
            }
        }

        inflate.selectScope.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }


        inflate.selectedFW.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                if (isCheck) {
                } else {
                }
            }
        }

        inflate.selectFW.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun disableView() {
        inflate.apply {
            selectCCDs.isEnabled = false
            selectedCCDs.isEnabled = false
            selectedCCDs.isChecked = false
            selectFW.isEnabled = false
            selectedFW.isEnabled = false
            selectedFW.isChecked = false
            selectScope.isEnabled = false
            selectedScope.isEnabled = false
            selectedScope.isChecked = false
        }

        inflatel.guidingDevice.apply {
            connect.isEnabled = false
            connect.isChecked = false
            select.isEnabled = false
        }
        inflatel.mainDevice.apply {
            connect.isEnabled = false
            connect.isChecked = false
            select.isEnabled = false
        }
        inflatel.camerasetting.apply {
            cameramodechoose.isEnabled = false
            connectall.isEnabled = false
        }
    }


    suspend fun loadEquipment() {

        var ccds = withContext(Dispatchers.IO) { guider.getEquipmentCCDs() }
        adapterCCDs.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        for (i in 0 until ccds.size)
            adapterCCDs.insert(ccds.get(i), i)
        inflate.selectCCDs.adapter = adapterCCDs
        adapterCCDs.notifyDataSetChanged()
        for (i in 0 until ccds.size) {
            if ("ToupCam" == ccds.get(i)) {
                inflate.selectCCDs.setSelection(i)
                break
            }
        }

        var scopes = withContext(Dispatchers.IO) { guider.getEquipmentScopes() }
        adapterScope.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        for (i in 0 until scopes.size)
            adapterScope.insert(scopes.get(i), i)
        inflate.selectScope.adapter = adapterScope
        adapterScope.notifyDataSetChanged()

        var fws = withContext(Dispatchers.IO) { guider.getEquipmentFWs() }
        adapterFW.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        for (i in 0 until fws.size)
            adapterFW.insert(fws.get(i), i)
        inflate.selectFW.adapter = adapterFW
        adapterFW.notifyDataSetChanged()
    }


    public fun updateUi() {
        inflate.edittextHost.setText(hostAddr)
    }

    private class MySpinnerAdapter(val context: Context) : BaseAdapter() {
        private val TAG = "MySpinnerAdapter"
        var mList = ArrayList<Profile>()

        override fun getCount(): Int {
            return mList.size
        }

        override fun getItem(position: Int): Profile {
            return mList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var textView: TextView =
                (convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.simple_spinner_item, parent, false)) as TextView
            var item = getItem(position)
            textView.setText(item.name)
            return textView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var textView: TextView = convertView?.run { this as TextView } ?: TextView(context)
            var item = getItem(position)
            textView.setText(item.name)
            return textView
        }
    }


    fun setPanel() {
        guideResolAdapterList.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        inflatel.selectResolution.adapter = guideResolAdapterList
        inflatel.selectResolution.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    GlobalScope.launch {
                        guider.setResolution(position)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    suspend fun updateResolution(open: Boolean) {
        if (open) {
            val resolutions = withContext(Dispatchers.IO) { guider.getResolutions() }
            val currentResolution = withContext(Dispatchers.IO) { guider.getResolution() }
            val listGuideResolution = ArrayList<String>()
            var listrel: String
            for (i in 0 until resolutions.size) {
                listrel =
                    resolutions[i].getString("resX") + " x " + resolutions[i].getString("resY")
                listGuideResolution.add(listrel)
            }
            withContext(Dispatchers.Main) {
                guideResolAdapterList.apply {
                    clear()
                    addAll(listGuideResolution)
                    notifyDataSetChanged()
                }
                inflatel.selectResolution.setSelection(currentResolution)
            }
        } else {
            withContext(Dispatchers.Main) {
                guideResolAdapterList.apply {
                    clear()
                    notifyDataSetChanged()
                }
            }
        }
    }


    fun mainDevice() {
        mainAdapterList.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        inflatel.mainDevice.select.adapter = mainAdapterList
        inflatel.mainDevice.connect.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                try {
                    if (isCheck) {
                        mainDevice?.let {
                            withContext(Dispatchers.Main) {
                                if (guideIndex != noDataFlag) {
                                    var guideList = ArrayList<String>(listDevices)
                                    if (mainIndex != noDataFlag){
                                        guideList.remove(listDevices.get(mainIndex))
                                        if (guideIndex == mainIndex) {
                                            if (guideList.size != 0)
                                                guideIndex = listDevices.indexOf(guideList.get(0))
                                            else guideIndex = noDataFlag
                                        }
                                    }
                                    guideAdapterList.apply {
                                        clear()
                                        addAll(guideList)
                                        notifyDataSetChanged()
                                    }
                                    if (guideList.size != 0 ) inflatel.guidingDevice.select.setSelection( guideList.indexOf( listDevices.get(guideIndex) ) )
                                }

                                inflatel.mainDevice.select.isEnabled = false
                            }
                            if (!mainRecoveryFlag) {
                                withContext(Dispatchers.IO) {
                                    if (mainIndex != noDataFlag) {
                                        guider.select_camera("main", listDevices.get(mainIndex))
                                        guider.connectMain()
                                        updateStreamDevice()
                                        updateResolution(true)
                                    }
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            if (mainIndex != noDataFlag) {
                                if (listDevices.size == 1) guideIndex = mainIndex
                                guideAdapterList.apply {
                                    clear()
                                    addAll(listDevices)
                                    notifyDataSetChanged()
                                    inflatel.guidingDevice.select.setSelection(guideIndex)
                                }
                                withContext(Dispatchers.IO) {
                                    guider.disconnectMain()
                                }
                            }
                            inflatel.mainDevice.select.isEnabled = true
                            if (!mainRecoveryFlag) {
                                (context as MainActivity).stopStream()
/*                                try {
                                    launch(Dispatchers.IO) {
                                        guider.stopCapture()
                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }*/
                                try {
                                    launch(Dispatchers.IO) {
                                        guider.disconnectMain()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            updateResolution(false)
                        }
                    }
                    mainRecoveryFlag = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        inflatel.mainDevice.select.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    mainIndex = listDevices.indexOf(mainAdapterList.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    fun guideDevice() {
        guideAdapterList.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        inflatel.guidingDevice.select.adapter = guideAdapterList
        inflatel.guidingDevice.connect.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                try {
                    if (isCheck) {
                        guideDevice?.let {
                            withContext(Dispatchers.Main) {
                                if (mainIndex != noDataFlag) {
                                    var mainList = ArrayList<String>(listDevices)
                                    if (guideIndex != noDataFlag){
                                        mainList.remove(listDevices.get(guideIndex))
                                        if (guideIndex == mainIndex) {
                                            if (mainList.size != 0)
                                                mainIndex = listDevices.indexOf(mainList.get(0))
                                            else mainIndex = noDataFlag
                                        }
                                    }

                                    mainAdapterList.apply {
                                        clear()
                                        addAll(mainList)
                                        notifyDataSetChanged()
                                    }

                                    if (mainList.size != 0)inflatel.mainDevice.select.setSelection(mainList.indexOf(listDevices.get(mainIndex)))
                                }
                                inflatel.guidingDevice.select.isEnabled = false
                            }
                            if (!guideRecoveryFlag) {
                                withContext(Dispatchers.IO) {
                                    if (guideIndex != noDataFlag) {
                                        guider.select_camera("guide", listDevices.get(guideIndex))
                                        guider.connectGuide()
                                        updateStreamDevice()
                                        updateResolution(true)
                                    }
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            if (guideIndex != noDataFlag) {
                                if (listDevices.size == 1) mainIndex = guideIndex
                                mainAdapterList.apply {
                                    clear()
                                    addAll(listDevices)
                                    notifyDataSetChanged()
                                    inflatel.mainDevice.select.setSelection(mainIndex)
                                }
                                withContext(Dispatchers.IO) {
                                    guider.disconnectGuide()
                                }
                            }
                            inflatel.guidingDevice.select.isEnabled = true
                            if (!guideRecoveryFlag) {
                                (context as MainActivity).stopStream()
/*                                try {
                                    launch(Dispatchers.IO) {
                                        guider.stopCapture()
                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }*/
                                try {
                                    launch(Dispatchers.IO) {
                                        guider.disconnectGuide()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            updateResolution(false)
                        }
                    }
                    guideRecoveryFlag = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        inflatel.guidingDevice.select.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    guideIndex = listDevices.indexOf(guideAdapterList.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    val listDevices = ArrayList<String>()
    var mainIndex = -1
    var guideIndex = -1
    val noDataFlag = -1
    /*经过了这样一个函数，就可以得到了不同的id，以及对应的UI显示，列表显示。
    * 等一下路上列举各种测试用例*/
    fun updateDevice() {/*这一层的作用，是将设备转化出来新的，将对应的信息转化为id。此处的控制变量有列表，
    设备，连接状态。结果有 -1，id，id相同。要求必须产出正确的id，还有正确的图形，不管外部怎么变化。关键是第二次必须和其中的进行保持。自己操作的和自己进行保持。*/
        mainIndex = -1
        guideIndex = -1
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                recoveryDeviceStatus()
                listDevices.clear()
                listDevices.addAll(guider.devicesCurrentValid)
                if (listDevices.size == 0) return@withContext
                Log.e(TAG, "updateDevice: " + listDevices.toString())
                mainIndex = listDevices.indexOf(mainDevice)
                guideIndex = listDevices.indexOf(guideDevice)
                /*此处假设两者都没有，也就是说配置里面没有，配置里面只有一个，配置里面有两个，或者这两个都是一样的，或者配置里面的比
                * 列表里面的还要多，如果配置里面不包含呢。不包含的就用列表里面的来同步，这里一定要能够保证显示和id是一定正确对应的
                * 然后自己把各种可能性的对应理理清楚,*/

                Log.e(TAG, " updateDevice mainIndex: " + mainIndex + "guideIndex: " + guideIndex)
                if (mainIndex == noDataFlag) {//如果不被包含，就两种情况，没有配置或者配置与列表不匹配
                    val mainList = ArrayList<String>(listDevices)
                    if ((guideIndex != noDataFlag) && guideIsConnected) {
                        mainList.remove(listDevices.get(guideIndex))
                        mainAdapterList.apply {
                            clear()
                            addAll(mainList)
                            notifyDataSetChanged()
                        }
                    } else {
                        mainAdapterList.apply {
                            clear()
                            addAll(listDevices)
                            notifyDataSetChanged()
                        }
                    }
                    if (mainList.size != 0) mainIndex = listDevices.indexOf(mainList.get(0))//此处mainIndex 可能为-1
                } else {
                    val mainList = ArrayList<String>(listDevices)
                    if ((guideIndex != noDataFlag) && guideIsConnected) {
                        mainList.remove(listDevices.get(guideIndex))
                        if (guideIndex == mainIndex) {
                            if (mainList.size != 0)
                                mainIndex = listDevices.indexOf(mainList.get(0))
                            else mainIndex = noDataFlag
                        }
                    }
                    mainAdapterList.apply {
                        clear()
                        addAll(mainList)
                        notifyDataSetChanged()
                    }
                    if (mainIndex != noDataFlag)
                    inflatel.mainDevice.select.setSelection(mainList.indexOf(listDevices.get(mainIndex)))
                }

                if (guideIndex == noDataFlag) {
                    val guideList = ArrayList<String>(listDevices)
                    if ((mainIndex != noDataFlag) && mainIsConnected) {
                        guideList.remove(listDevices.get(mainIndex))
                        guideAdapterList.apply {
                            clear()
                            addAll(guideList)
                            notifyDataSetChanged()
                        }
                    } else {
                        guideAdapterList.apply {
                            clear()
                            addAll(listDevices)
                            notifyDataSetChanged()
                        }
                    }
                    if (guideList.size != 0) guideIndex = listDevices.indexOf(guideList.get(0))
                } else {
                    val guideList = ArrayList<String>(listDevices)
                    if ((mainIndex != noDataFlag) && mainIsConnected) {
                        guideList.remove(listDevices.get(mainIndex))
                        if (guideIndex == mainIndex){
                            if (guideList.size != 0){
                                guideIndex = listDevices.indexOf(guideList.get(0))
                            }
                            else guideIndex = noDataFlag
                        }
                    }
                    guideAdapterList.apply {
                        clear()
                        addAll(guideList)
                        notifyDataSetChanged()
                    }
                    if (guideList.size != 0)
                    inflatel.guidingDevice.select.setSelection(guideList.indexOf(listDevices.get(guideIndex)))
                }

                if (mainIsConnected != inflatel.mainDevice.connect.isChecked) {
                    mainRecoveryFlag = true
                    inflatel.mainDevice.connect.isChecked = mainIsConnected
                }
                if (guideIsConnected != inflatel.guidingDevice.connect.isChecked) {
                    guideRecoveryFlag = true
                    inflatel.guidingDevice.connect.isChecked = guideIsConnected
                }
                inflatel.mainDevice.select.isEnabled = !mainIsConnected
                inflatel.guidingDevice.select.isEnabled = !guideIsConnected
            }
        }
    }

    suspend fun recoveryDeviceStatus() {//这一层只考虑将网络的状态提取出来。
        var deviceStatus = withContext(Dispatchers.IO) { guider.getCurrentEquipment() }
        if (deviceStatus.getString("mainName") != "") {
            mainDevice = deviceStatus.getString("mainName")
            mainIsConnected = deviceStatus.getBoolean("mainStatus")
        } else {
            mainDevice = ""
            mainIsConnected = false
        }
        if (deviceStatus.getString("guideName") != "") {
            guideDevice = deviceStatus.getString("guideName")
            guideIsConnected = deviceStatus.getBoolean("guideStatus")
        } else {
            guideDevice = ""
            guideIsConnected = false
        }
        if (guideIsConnected)
            withContext(Dispatchers.IO) {
                updateResolution(true)
            }
        if (mainIsConnected)
            withContext(Dispatchers.IO) {
                updateResolution(true)
            }
    }


    fun pullStream() {
        inflatel.camerasetting.cameramodechoose.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    if (!streamDeviceFlag) {
                        if (isCheck) {
                            guider.setCameraModeMain()
                        } else {
                            guider.setCameraModeGuide()
                        }
                    }
                    streamDeviceFlag = false
                }
                withContext(Dispatchers.Main) {
                    if (isCheck) {
                        inflatel.camerasetting.cameramodetv.text = "主相机拉流"
                    } else {
                        inflatel.camerasetting.cameramodetv.text = "导星相机拉流"
                    }
                }
            }
        }

        inflatel.camerasetting.connectall.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                try {
                    if (isCheck) {
                        guider.connectEquipment()
                    } else {
                        guider.disconnectEquipment()
                        withContext(Dispatchers.Main) {
                            inflatel.guidingDevice.connect.isChecked = false
                            inflatel.mainDevice.connect.isChecked = false
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun updateCCDSStatus(size: Int) {
        var flag = size != 0
        if (flag != inflatel.popuwindown.selectedCCDs.isChecked) {
            ccdsFlag = true
            inflatel.popuwindown.selectedCCDs.isChecked = flag
        }
        if (flag) {
            inflatel.mainDevice.connect.isEnabled = true
            inflatel.guidingDevice.connect.isEnabled = true
        } else {
            inflatel.mainDevice.connect.isEnabled = false
            inflatel.guidingDevice.connect.isEnabled = false
        }
    }

    fun unloadEquipment() {
        adapterCCDs.clear()
        adapterCCDs.notifyDataSetChanged()

        adapterScope.clear()
        adapterScope.notifyDataSetChanged()

        adapterFW.clear()
        adapterFW.notifyDataSetChanged()

        clearDeviceCache()
    }

    private fun clearDeviceCache() {
        guideResolAdapterList.clear()
        guideResolAdapterList.notifyDataSetChanged()

        mainResolAdapterList.clear()
        mainResolAdapterList.notifyDataSetChanged()

        listDevices.clear()
        mainIndex = -1
        guideIndex = -1
        mainAdapterList.clear()
        mainAdapterList.notifyDataSetChanged()
        guideAdapterList.clear()
        guideAdapterList.notifyDataSetChanged()
    }

    fun syncCameraMode(mode: Boolean) {
        if (mode != inflatel.camerasetting.cameramodechoose.isChecked) {
            streamDeviceFlag = true
            inflatel.camerasetting.cameramodechoose.isChecked = mode
        }
    }

    fun syncResolution(device: String, index: Int) {
        GlobalScope.launch {
            updateResolution(true)
        }
    }

    fun updateStreamDevice() {
        GlobalScope.launch {
            var streamDevice = withContext(Dispatchers.IO) { guider.isCameraMainMode() }
            withContext(Dispatchers.Main) {
                if (streamDevice != inflatel.camerasetting.cameramodechoose.isChecked) {
                    streamDeviceFlag = true
                    inflatel.camerasetting.cameramodechoose.isChecked = streamDevice
                }
            }
        }
    }
}

