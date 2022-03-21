package com.kaya.basicmodeluitwo.ui.home.commend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaya.basicmodeluitwo.R
import com.kaya.basicmodeluitwo.ui.common.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

class CommendFragment :BaseFragment() {
    private lateinit var adapter: CommendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_refresh_layout, container, false))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        adapter = CommendAdapter(this, viewModel.dataList)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    companion object {

        fun newInstance() = CommendFragment()
    }
}