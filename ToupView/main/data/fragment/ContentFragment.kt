package com.example.astroclient.fragment

import android.os.Bundle
import com.example.astroclient.R
import com.example.astroclient.databinding.FragmentContentBinding


class ContentFragment : BaseFragment<FragmentContentBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_content

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initBanner()
    }

    private fun initBanner() {
    }

    private fun initRecyclerView() {
    }

}