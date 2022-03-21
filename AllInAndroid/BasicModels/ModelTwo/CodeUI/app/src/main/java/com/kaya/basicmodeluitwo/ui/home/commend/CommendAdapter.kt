package com.kaya.basicmodeluitwo.ui.home.commend

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaya.basicmodeluitwo.logic.model.HomePageRecommend
import com.kaya.basicmodeluitwo.ui.common.holder.RecyclerViewHelp

class CommendAdapter(val fragment: CommendFragment, val dataList: List<HomePageRecommend.Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) = RecyclerViewHelp.getItemViewType(dataList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecyclerViewHelp.getViewHolder(parent, viewType)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}