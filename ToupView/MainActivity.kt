package com.example.astroclient

import android.graphics.Matrix
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.astroclient.activity.SettingActivity
import com.example.astroclient.databinding.ActivityMainBinding
import com.example.astroclient.util.inflate
import com.example.astroclient.util.loge
import com.example.astroclient.util.logi
import com.example.astroclient.view.TpRender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    val binding: ActivityMainBinding by inflate(ActivityMainBinding::inflate)
    lateinit var connectPopwindow: ConnectPopwindow
    lateinit var gestureDetector: GestureDetector
    lateinit var scaleGestureDetector: ScaleGestureDetector
    lateinit var dialog: AlertDialog

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
    }

    lateinit var alertDialogBuilder: AlertDialog.Builder
    var alertDialog: AlertDialog? = null

    fun selectCamera() {
        val itemsList = connectPopwindow.guider.devicesCurrentValid
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
        init()
        mLib = TpLib.getInstance()
        mLib?.Init()
        mLib?.setPreviewHandler(mHandler)
        connectPopwindow.guider.setPreviewHandler(mHandler)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
    var matrix: Matrix? = null
    var currentScale = 1f
    var opened: Boolean? = false
    var size: Array<Int>? = null
    private fun init() {
        matrix = Matrix()
        connectPopwindow = ConnectPopwindow(this)
        connectPopwindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        connectPopwindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        scaleGestureDetector = ScaleGestureDetector(this,object:
            ScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(p0: ScaleGestureDetector): Boolean {
                binding.surface.render.scale(p0.scaleFactor,p0.focusX,p0.focusY)
                if(p0.scaleFactor > 2){

                    Toast.makeText(App.instance,"放大了两倍",Toast.LENGTH_SHORT).show()
                    return true;
                }
                else if (p0.scaleFactor < 0.5)
                {
                    Toast.makeText(App.instance,"缩小了两倍",Toast.LENGTH_SHORT).show()
                    return true;
                }
                return false
            }

            override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
                return true
            }

            override fun onScaleEnd(p0: ScaleGestureDetector?) {

            }


        })
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                Toast.makeText(App.instance,"点击了一下",Toast.LENGTH_SHORT).show()
                if (e != null && connectPopwindow.guider.isConnected) {
                    GlobalScope.launch {
                        try {
                            var x = e.x - binding.surface.render.mBoundLeft
                            var y = e.y - binding.surface.render.mBoundTop

                            withContext(Dispatchers.Main){
                            }
                            if (x < 0 || x > binding.surface.render.mBoundRight || y < 0 || y > binding.surface.render.mBoundBottom) {
                                loge(TAG, "out of bound ${e.x} ${e.y} $x $y")
                                return@launch
                            }

                            val points = floatArrayOf(
                                x,
                                y
                            )
                            binding.surface.render.mapPoints(points)

                            loge(TAG, e.x, e.y, points[0], points[1])
                            connectPopwindow.guider.setLockPosition(points[0], points[1], true)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                return true
            }

        })

        var ctlBar = binding.ctlbar
        ctlBar.btnUsb.setOnClickListener {
            connectPopwindow.showAsDropDown(it)
            connectPopwindow.updateUi()
        }

        ctlBar.btnLoop.setOnClickListener {
            GlobalScope.launch {
                try {
                    if (opened == true) {
                        mLib?.StartStream()
                    } else {
                        connectPopwindow.guider.connectEquipment()
                        connectPopwindow.guider.loop()
                        opened = mLib?.OpenCamera(connectPopwindow.hostAddr)
                        if (opened == true)
                            logi(TAG, "OpenCamera Success!")
                        else
                            logi(TAG, "OpenCamera Failed!")
                    }

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
                }
            }
        }

        ctlBar.btnGuide.setOnClickListener {
            GlobalScope.launch {
                try {
                    connectPopwindow.guider.guide(1.5, 10.0, 60.0)
                } catch (e: Exception) {
                    e.printStackTrace()
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

        ctlBar.btnCamSetup.setOnClickListener {
            SettingActivity.actionStart(this)
        }
        ctlBar.btnBrain.setOnClickListener {
            GlobalScope.launch {
            }
        }
        ctlBar.btnCamSetup.setOnClickListener {
            GlobalScope.launch {
                try {
                    connectPopwindow.guider.shutdown()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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
}
