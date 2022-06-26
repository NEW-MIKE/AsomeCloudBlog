package com.example.astroclient.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.astroclient.util.Guider

open abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    abstract val layoutId: Int
    lateinit var guiderf: Guider
    lateinit var binding: T
    val loadingState = MutableLiveData<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }


    fun setGuider(guider: Guider){
        guiderf = guider
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}