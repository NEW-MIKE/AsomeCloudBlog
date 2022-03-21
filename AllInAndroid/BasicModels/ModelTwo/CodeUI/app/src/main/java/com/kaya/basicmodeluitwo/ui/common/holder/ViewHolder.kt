package com.kaya.basicmodeluitwo.ui.common.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaya.basicmodeluitwo.Const.ItemViewType.Companion.HORIZONTAL_SCROLL_CARD
import com.kaya.basicmodeluitwo.Const.ItemViewType.Companion.TEXT_CARD_HEADER4
import com.kaya.basicmodeluitwo.Const.ItemViewType.Companion.TEXT_CARD_HEADER5
import com.kaya.basicmodeluitwo.Const.ItemViewType.Companion.TEXT_CARD_HEADER7
import com.kaya.basicmodeluitwo.Const.ItemViewType.Companion.UNKNOWN
import com.kaya.basicmodeluitwo.R
import com.kaya.basicmodeluitwo.logic.model.HomePageRecommend

/**
 * 项目通用ViewHolder，集中统一管理。
 *
 * @author vipyinzhiwei
 * @since  2020/5/1
 */

/**
 * 未知类型，占位进行容错处理。
 */
class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

class TextCardViewHeader4ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle4 = view.findViewById<TextView>(R.id.tvTitle4)
    val ivInto4 = view.findViewById<ImageView>(R.id.ivInto4)
}

class TextCardViewHeader5ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle5 = view.findViewById<TextView>(R.id.tvTitle5)
    val tvFollow = view.findViewById<TextView>(R.id.tvFollow)
    val ivInto5 = view.findViewById<ImageView>(R.id.ivInto5)
}

/**
 * RecyclerView帮助类，获取通用ViewHolder或ItemViewType。
 */
object RecyclerViewHelp {

    fun getViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {

//        TEXT_CARD_HEADER4 -> TextCardViewHeader4ViewHolder(R.layout.item_text_card_type_header_four.inflate(parent))
//
//        TEXT_CARD_HEADER5 -> TextCardViewHeader5ViewHolder(R.layout.item_text_card_type_header_five.inflate(parent))

        else -> EmptyViewHolder(View(parent.context))
    }

    fun getItemViewType(type: String, dataType: String) = when (type) {

        "horizontalScrollCard" -> {
            when (dataType) {
                "HorizontalScrollCard" -> HORIZONTAL_SCROLL_CARD
                else -> UNKNOWN
            }
        }
        /*"textCard" -> {
            when (item.data.type) {
                "header5" -> TEXT_CARD_HEADER5
                "header7" -> TEXT_CARD_HEADER7
                "header8" -> TEXT_CARD_HEADER8
                "footer2" -> TEXT_CARD_FOOTER2
                "footer3" -> TEXT_CARD_FOOTER3
                else -> UNKNOWN
            }
        }*/
        else -> UNKNOWN
    }

    private fun getTextCardType(type: String) = when (type) {
        "header4" -> TEXT_CARD_HEADER4
        "header5" -> TEXT_CARD_HEADER5
        "header7" -> TEXT_CARD_HEADER7
        else -> UNKNOWN
    }
/*此处是一个多态的设计*/
//    fun getItemViewType(item: Discovery.Item): Int {
//        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
//    }

    fun getItemViewType(item: HomePageRecommend.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

}