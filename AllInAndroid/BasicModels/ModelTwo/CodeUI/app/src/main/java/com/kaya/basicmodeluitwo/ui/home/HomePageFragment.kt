package com.kaya.basicmodeluitwo.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flyco.tablayout.listener.CustomTabEntity
import com.kaya.basicmodeluitwo.R
import com.kaya.basicmodeluitwo.logic.model.TabEntity
import com.kaya.basicmodeluitwo.ui.common.ui.BaseViewPagerFragment
import com.kaya.basicmodeluitwo.ui.home.commend.CommendFragment
import com.kaya.basicmodeluitwo.util.GlobalUtil


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomePageFragment : BaseViewPagerFragment() {
    override val createTitles = ArrayList<CustomTabEntity>().apply {
        add(TabEntity(GlobalUtil.getString(R.string.discovery)))
        add(TabEntity(GlobalUtil.getString(R.string.commend)))
        add(TabEntity(GlobalUtil.getString(R.string.daily)))
    }

    override val createFragments: Array<Fragment> = arrayOf(CommendFragment.newInstance(), CommendFragment.newInstance(), CommendFragment.newInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_page, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager?.currentItem = 1
    }

    companion object {

        fun newInstance() = HomePageFragment()
    }
}