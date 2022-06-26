package com.example.astroclient

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astroclient.adapter.LogAdapter
import com.example.astroclient.databinding.ActivityMainBinding
import com.example.astroclient.fragment.SettingOneFragment
import com.example.astroclient.fragment.TabItem
import com.example.astroclient.manager.ChartManager
import com.example.astroclient.util.*
import com.example.astroclient.view.TpRender
import com.github.mikephil.charting.charts.CombinedChart
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.view_tab.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    /*chart data start*/

    private lateinit var mCombinedChart: CombinedChart

    private lateinit var ChartManager: ChartManager
    private val maxisMaximum = 60f

    val logList = ArrayList<String>()
    private lateinit var logAdapter:LogAdapter
    val valuesDX = ArrayList<Float?>()
    val valuesDY = ArrayList<Float?>()
    val valuesStarMass = ArrayList<Float?>()
    val valuesSNR = ArrayList<Float?>()
    val valuesRADistanceRaw = ArrayList<Float?>()
    val valuesDecDistanceRaw = ArrayList<Float?>()
    val valuesDECDuration = ArrayList<Float>()
    val valuesRADuration = ArrayList<Float>()
    var indexChart: Int = 0

    val barNames = ArrayList<String>()
    val colors = ArrayList<Int>()
    val lineNames = ArrayList<String>()

    val yLineDatas = ArrayList<List<Float?>>()

    fun CombineChart() {
        val yBarDatas = ArrayList<List<Float>>()
        val chartData = connectPopwindow.guider.chartData
        if (indexChart > maxisMaximum + 1) {
            indexChart = maxisMaximum.toInt() + 1
            valuesDX.removeAt(0)
            valuesDY.removeAt(0)
            valuesSNR.removeAt(0)
            valuesStarMass.removeAt(0)
            valuesDECDuration.removeAt(0)
            valuesRADuration.removeAt(0)
            valuesRADistanceRaw.removeAt(0)
            valuesDecDistanceRaw.removeAt(0)
        }
        indexChart++
        valuesDX.add(chartData.dx?.toFloat())
        valuesDY.add(chartData.dy?.toFloat())
        valuesStarMass.add(chartData.StarMass?.let { it.toFloat() / 100000 })
        valuesSNR.add(chartData.SNR?.let { it.toFloat() / 10 })
        valuesRADistanceRaw.add(chartData.RADistanceRaw?.toFloat())
        valuesDecDistanceRaw.add(chartData.DecDistanceRaw?.toFloat())
        var duration = 0
        chartData.RADuration?.let {
            if (chartData.RADirection == "West") duration = it
            else duration = -it
        }
        valuesRADuration.add(duration.toFloat() / 20)
        duration = 0
        chartData.DECDuration?.let {
            if (chartData.DECDirection == "North") duration = it
            else duration = -it
        }

        valuesDECDuration.add(duration.toFloat() / 20)
        yBarDatas.add(valuesRADuration)
        yBarDatas.add(valuesDECDuration)

        yLineDatas.clear()
        yLineDatas.add(valuesDX)
        yLineDatas.add(valuesDY)
        yLineDatas.add(valuesStarMass)
        yLineDatas.add(valuesSNR)
        yLineDatas.add(valuesRADistanceRaw)
        yLineDatas.add(valuesDecDistanceRaw)

        ChartManager.showCombinedChart(
            maxisMaximum, yBarDatas, yLineDatas, barNames, lineNames,
            colors, colors
        )
    }

    fun initChart() {
        barNames.add("RADuration")
        barNames.add("DECDuration")
        colors.add(Color.BLUE)
        colors.add(Color.RED)
        colors.add(Color.YELLOW)
        colors.add(Color.YELLOW)
        colors.add(Color.WHITE)
        colors.add(Color.GREEN)
        lineNames.add("dx")
        lineNames.add("dy")
        lineNames.add("StarMass")
        lineNames.add("SNR")
        lineNames.add("RADistanceRaw")
        lineNames.add("DecDistanceRaw")
        mCombinedChart = findViewById(R.id.chart)

        ChartManager = ChartManager(mCombinedChart)
/*        GlobalScope.launch {
            connectPopwindow.guider.connect()
        }*/
        CombineChart()
    }
    /*chart data end */

    var isLongClick = false
    val binding: ActivityMainBinding by inflate(ActivityMainBinding::inflate)
    lateinit var connectPopwindow: ConnectPopwindow
    lateinit var gestureDetector: GestureDetector
    lateinit var gestureScrollDetector: GestureDetector
    lateinit var scaleGestureDetector: ScaleGestureDetector

    var xScalePoint = 0F
    var yScalePoint = 0F

    var mLib: TpLib? = null;
    var mHandler: TpHandler = TpHandler(this);
    private fun update() {
        binding.surface.requestRender()
    }

    var mWidth = 0
    var mHeight = 0
    fun update(width: Int, height: Int) {
        if (mWidth !== width || mHeight !== height) {
            initSize(width, height)
            mWidth = width
            mHeight = height
        }
        update()
        val size = mLib!!.GetLiveSize()
        val pixels = mLib!!.GetPixelsData()
        val bitmap = BitmapUtil.rgb2Bitmap(pixels,size.width,size.height)
        try {
            val newbitmap = BitmapUtil.bitMapScale(bitmap!!,1F);
            binding.iv.setImageBitmap(newbitmap)
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    lateinit var alertDialogBuilder: AlertDialog.Builder
    var alertDialog: AlertDialog? = null

    fun selectCamera() {
        val itemsList = connectPopwindow.guider.devicesCurrentValid
        if (itemsList.size == 0) return
        val items = connectPopwindow.guider.devicesCurrentValid.toTypedArray()
        var index = 0
        alertDialog?.dismiss()
        alertDialog = alertDialogBuilder?.run {
            setTitle("请选择相机")
            setSingleChoiceItems(items, 0) { dialog, which ->
                index = which
            }
            setPositiveButton("OK") { dialog, which ->
                GlobalScope.launch {
                    try {
                        connectPopwindow.guider.select_camera("guide", itemsList[index])
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            setNegativeButton("Cancel") { dialog, which ->

            }
            show()
        }
    }

    private fun initSize(videoWidth: Int, videoHeight: Int) {
        binding.surface.render.setCurrentVideoSize(videoWidth, videoHeight) // todo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DisplayUtil.setCustomDensity(this, App.instance)
        init()
        mLib = TpLib.getInstance()
        mLib?.Init()
        mLib?.setPreviewHandler(mHandler)
        connectPopwindow.guider.setPreviewHandler(mHandler)
        initChart()
        mCombinedChart.visibility = View.INVISIBLE
        initFragement()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!!.getAction() == MotionEvent.ACTION_UP ) {
            Log.d("TAG", "DOWN  or CANCEL ");
            binding.iv.visibility = View.INVISIBLE;
            isLongClick = false
        }
        if (event!!.action == MotionEvent.ACTION_MOVE){
            if(event.x < binding.iv.right && event.y > binding.iv.top){
                binding.iv.x = 900F
            }
            else if (event.x > binding.iv.left && event.y > binding.iv.top){
                binding.iv.x = 0F
            }
            if(isLongClick)
            gestureScrollDetector.onTouchEvent(event)
        }
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun getClickPosition(e: MotionEvent) : FloatArray{
        var oldPoint = binding.surface.render.oldPoints
        var newPoint = binding.surface.render.newPoints

        val mBoundRight = binding.surface.render.mBoundRight
        val mBoundLeft = binding.surface.render.mBoundLeft
        val mBoundTop = binding.surface.render.mBoundTop
        val mBoundBottom = binding.surface.render.mBoundBottom

        val xscale = (mBoundRight - mBoundLeft)/(oldPoint[2] - oldPoint[0])
        val yscale = (mBoundBottom - mBoundTop)/(oldPoint[5] - oldPoint[1])

        val centerPoint = floatArrayOf((mBoundRight - mBoundLeft)/2+mBoundLeft,
            (mBoundBottom - mBoundTop)/2+mBoundTop)

        val scaleClickPoint = floatArrayOf((e.x - centerPoint[0])/xscale,
            -(e.y - centerPoint[1])/yscale)

        val clickPointScale = floatArrayOf((scaleClickPoint[0] - newPoint[4])/(newPoint[6] - newPoint[4]),
            (newPoint[5] - scaleClickPoint[1])/(newPoint[5] - newPoint[1]))

        val newClickPoint = floatArrayOf((mBoundRight - mBoundLeft) * clickPointScale[0] + mBoundLeft,
            (mBoundBottom - mBoundTop) * clickPointScale[1] )
        return newClickPoint
    }
    // var opened: Boolean? = false
    var size: Array<Int>? = null
    var pt = PointF()
    private fun isClickInScaleArea(e: MotionEvent):Boolean{
        if(!binding.iv.isInvisible){
            if(e.x > binding.iv.left && e.x < binding.iv.right && e.y > binding.iv.top && e.y < binding.iv.bottom){
                return true
            }
        }
        return false
    }

    fun getScalePoint(e: MotionEvent,xLength:Float,yLength:Float,oldXpoint:Float,oldYpoint:Float):FloatArray{
        val ivLeftPoint = binding.iv.left
        val ivRightPoint = binding.iv.right
        val ivTopPoint = binding.iv.top
        val ivBottomPoint = binding.iv.bottom

        val xscale = (e.x - ivLeftPoint)/(ivRightPoint - ivLeftPoint)
        val yscale = (e.y - ivTopPoint)/(ivBottomPoint - ivTopPoint)

        val xDistance = xLength * xscale
        val yDistance = yLength * yscale

        val clickPoint = floatArrayOf(oldXpoint - xLength/2 + xDistance,oldYpoint - yLength/2 + yDistance)
        return clickPoint
    }

    private fun init() {
        alertDialogBuilder = AlertDialog.Builder(this)
        connectPopwindow = ConnectPopwindow(this)
        connectPopwindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        connectPopwindow.height = ViewGroup.LayoutParams.WRAP_CONTENT

        connectPopwindow.guider.setOnListener(object : CallbackListener{
            override fun setMessage(message: String) {
                loge("dd",message)
                logList.add(message)
                logAdapter.notifyDataSetChanged()
                binding.logv.smoothScrollToPosition(logList.size-1)
            }
        })

        val setlayoutManager = LinearLayoutManager(this)
        setlayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.logv.apply {
            layoutManager = setlayoutManager
            logAdapter = LogAdapter(logList)
            adapter = logAdapter
        }
        scaleGestureDetector = ScaleGestureDetector(this, object :
            ScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(p0: ScaleGestureDetector?): Boolean {
                return false
            }

            override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
                return true
            }

            override fun onScaleEnd(p0: ScaleGestureDetector) {
                binding.surface.render.scale(p0.scaleFactor, p0.focusX, p0.focusY)
            }


        })
        gestureScrollDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                binding.surface.render.trans(-distanceX,distanceY)
                Log.e(TAG, "onScroll  "+ "e.x  " +e2!!.x +"e.y   "+e2!!.y )

                binding.iv.visibility = View.VISIBLE
                BitmapUtil.setClickPosition(e2,binding.surface.render.mBoundLeft,binding.surface.render.mBoundRight,binding.surface.render.mBoundTop,binding.surface.render.mBoundBottom)

                return false
            }
        })
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                var pp = getClickPosition(e!!)
                if (isClickInScaleArea(e!!)){
                    val point = getScalePoint(e!!,15f,15f,xScalePoint,yScalePoint)

                    loge("point",point[0].toString() +"ypoint is "+point[1].toString())
                    return true
                }
                loge("ex ",e!!.x.toString())
                loge("ey",e!!.y.toString())
                loge("getClickPosition ex",pp[0].toString())
                loge("getClickPosition ey",pp[1].toString())
                if (e != null && connectPopwindow.guider.isConnected) {
                    GlobalScope.launch {
                        try {
                            var x = e.x - binding.surface.render.mBoundLeft
                            var y = e.y - binding.surface.render.mBoundTop


                            if (x < 0 || e.x > binding.surface.render.mBoundRight || y < 0 || e.y > binding.surface.render.mBoundBottom) {
                                loge(TAG, "out of bound ${e.x} ${e.y} $x $y")
                                return@launch
                            }
                            val points = floatArrayOf(
                                x,
                                y
                            )
                            binding.surface.render.mapPoints(points)
                            pt.x = x
                            pt.y = y
                            loge(TAG, e.x, e.y, points[0], points[1])
                            connectPopwindow.guider.setLockPosition(points[0], points[1], true)
                            connectPopwindow.guider.findStarByPos(pt)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                binding.iv.visibility = View.INVISIBLE
                return true
            }
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                binding.surface.render.trans(-distanceX,distanceY)
                Log.e(TAG, "onScroll  "+ "e.x  " +e2!!.x +"e.y   "+e2!!.y )
                BitmapUtil.setClickPosition(e2,binding.surface.render.mBoundLeft,binding.surface.render.mBoundRight,binding.surface.render.mBoundTop,binding.surface.render.mBoundBottom)

                // gestureDetector.setIsLongpressEnabled(true)
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                super.onLongPress(e)
                if (e != null) {
                    isLongClick = true
                    Log.e(TAG, "onLongPress  "+ "e.x  " +e.x +"e.y   "+e.y )
                    xScalePoint = e.x
                    yScalePoint = e.y
                    binding.iv.visibility = View.VISIBLE
                    BitmapUtil.setClickPosition(e,binding.surface.render.mBoundLeft,binding.surface.render.mBoundRight,binding.surface.render.mBoundTop,binding.surface.render.mBoundBottom)
                }
               // gestureDetector.setIsLongpressEnabled(false)
            }
        })


        var ctlBar = binding.ctlbar
        ctlBar.btnUsb.setOnClickListener {
            connectPopwindow.showAsDropDown(it)
            connectPopwindow.updateUi()
        }

        ctlBar.btnSetting.setOnClickListener {
            if(binding.fragmentContainer.visibility == View.VISIBLE){
                binding.fragmentContainer.visibility = View.GONE
                loge("dd","View.VISIBLE")
            }
            else
            binding.fragmentContainer.visibility = View.VISIBLE

            loge("dd",binding.fragmentContainer.visibility.toString())
        }
        ctlBar.btnLoop.setOnClickListener {
            GlobalScope.launch {
                try {
                    connectPopwindow.guider.connectEquipment()
                    connectPopwindow.guider.loop()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.surface.render.renderBlock = object : TpRender.RenderInterface {
            override fun haveData(): Boolean {
                return mLib?.HaveData() ?: false
            }
            override fun render(texture: Int) {
                mLib?.Step(texture)
            }
        }

        ctlBar.btnAutoStar.setOnClickListener {
            GlobalScope.launch {
                try {
                    var findStar = connectPopwindow.guider.findStar()
                    loge(TAG, findStar)
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(App.instance, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        ctlBar.btnGuide.setOnClickListener {
            GlobalScope.launch {
                try {
                    connectPopwindow.guider.guide(1.5, 10.0, 60.0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(App.instance, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        ctlBar.btnStop.setOnClickListener {
            GlobalScope.launch {
                try {
                    connectPopwindow.guider.stopCapture()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        ctlBar.btnBrain.setOnClickListener {
            mCombinedChart.apply {
                if (isInvisible) visibility = View.VISIBLE
                else visibility = View.INVISIBLE
            }
            GlobalScope.launch {
            }
        }
        ctlBar.btnStartStream.setOnClickListener {
            if (!connectPopwindow.guider.isConnected) {
                Toast.makeText(App.instance, "请先开启网络连接", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            GlobalScope.launch {
                try {
                    if (connectPopwindow.opened == true) {
                        mLib?.StartStream()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(App.instance, "StartStream", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (connectPopwindow.guider.AppState == "Looping") {
                            connectPopwindow.opened = mLib?.OpenCamera(connectPopwindow.hostAddr)
                            if (connectPopwindow.opened == true) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        App.instance,
                                        "OpenCamera Success!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                logi(TAG, "OpenCamera Success!")
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        App.instance,
                                        "OpenCamera Failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                logi(TAG, "OpenCamera Failed!")
                            }
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        ctlBar.btnCamSetup.setOnClickListener {
/*            GlobalScope.launch {
                try {
                    connectPopwindow.guider.shutdown()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }*/
            binding.surface.render.resetMatrix()
        }

        var mAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf("10", "100", "1000")
        )
        mAdapter.setDropDownViewResource(
            android.R.layout.simple_list_item_single_choice
        )

        ctlBar.spinnerExpose.adapter = mAdapter
        ctlBar.spinnerExpose.setSelection(1)
        ctlBar.spinnerExpose.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    GlobalScope.launch {
                        var item = mAdapter.getItem(position)
                        var newExp = item?.toInt() ?: 10
                        logi(TAG, newExp)
                        connectPopwindow.guider.setExposure(newExp)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }


    private val fragments = ArrayList<Fragment>()
    private val tabs = arrayOf(
        TabItem("返回", SettingOneFragment::class.java),
        TabItem("场景1", SettingOneFragment::class.java),
        TabItem("场景2", SettingOneFragment::class.java)
    )
    private fun initFragement(){
        if (fragments.isEmpty()) {
            tabs.forEach {
                val f = it.fragmentCls.newInstance()
                fragments.add(f)
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEach {
            if (!it.isAdded) transaction.add(
                R.id.fl_content, it, it.javaClass
                    .simpleName
            ).hide(it)
        }
        transaction.commit()

        binding.tabLayout.run {
            tabs.forEach {
                addTab(newTab().setCustomView(getTabView(it)))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}

                override fun onTabUnselected(p0: TabLayout.Tab?) {}

                override fun onTabSelected(p0: TabLayout.Tab) {
                    initTab(p0.position)
                }
            })
            getTabAt(1)?.select()
        }
        initTab(1)
    }
    private fun getTabView(it: TabItem): View {
        val v = LayoutInflater.from(this).inflate(R.layout.view_tab, null)
        v.tab_name.text = it.name
        return v
    }

    private fun initTab(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (index == position) {
                transaction.show(fragment)
            } else {
                transaction.hide(fragment)
            }
        }
        transaction.commit()
    }
}
