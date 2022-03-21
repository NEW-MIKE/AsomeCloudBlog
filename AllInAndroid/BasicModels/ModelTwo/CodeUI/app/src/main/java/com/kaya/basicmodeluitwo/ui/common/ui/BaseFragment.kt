package com.kaya.basicmodeluitwo.ui.common.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaya.basicmodeluitwo.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseFragment : Fragment() {
    /**
     * Fragment中inflate出来的布局。
     */
    protected var rootView: View? = null
    /**
     * 依附的Activity
     */
    lateinit var activity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 缓存当前依附的activity
        activity = getActivity()!!
    }
    fun onCreateView(view: View): View {
        rootView = view
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}