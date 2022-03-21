package com.kaya.basicmodeluitwo.ui.common.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.kaya.basicmodeluitwo.R
import com.kaya.basicmodeluitwo.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * 应用程序中所有Activity的基类。
 *
 * @author vipyinzhiwei
 * @since  2020/4/29
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    /**
     * 判断当前Activity是否在前台。
     */
    protected var isActive: Boolean = false

    /**
     * 当前Activity的实例。
     */
    protected var activity: Activity? = null

    /** 当前Activity的弱引用，防止内存泄露  */
    private var activityWR: WeakReference<Activity>? = null

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        logD(TAG, "BaseActivity-->onCreate()")

        activity = this
        activityWR = WeakReference(activity!!)
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        isActive = true
        //MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        isActive = false
       // MobclickAgent.onPause(this)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
        activity = null
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setStatusBarBackground(R.color.colorPrimaryDark)
        setupViews()
    }

    override fun setContentView(layoutView: View) {
        super.setContentView(layoutView)
        setStatusBarBackground(R.color.colorPrimaryDark)
        setupViews()
    }

    protected open fun setupViews() {
//        val navigateBefore = findViewById<ImageView>(R.id.ivNavigateBefore)
//        val tvTitle = findViewById<TextView>(R.id.tvTitle)
//        navigateBefore?.setOnClickListener { finish() }
//        tvTitle?.isSelected = true  //获取焦点，实现跑马灯效果。

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    open fun onMessageEvent(messageEvent: MessageEvent) {
//
//    }

    /**
     * 设置状态栏背景色
     */
    open fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
//        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f).statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }

    /**
     * 调用系统原生分享
     *
     * @param shareContent 分享内容
     * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
     */
    protected fun share(shareContent: String, shareType: Int) {
//        ShareUtil.share(this, shareContent, shareType)
    }

    /**
     * 弹出分享对话框
     *
     * @param shareContent 分享内容
     */
    protected fun showDialogShare(shareContent: String) {
//        com.eyepetizer.android.extension.showDialogShare(this, shareContent)
    }
}
