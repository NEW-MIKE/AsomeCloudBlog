package com.kaya.basicmodeluitwo.ui

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.kaya.basicmodeluitwo.R
import com.kaya.basicmodeluitwo.event.MessageEvent
import com.kaya.basicmodeluitwo.event.RefreshEvent
import com.kaya.basicmodeluitwo.extension.setOnClickListener
import com.kaya.basicmodeluitwo.ui.common.ui.BaseActivity
import com.kaya.basicmodeluitwo.ui.home.HomePageFragment
import com.kaya.basicmodeluitwo.ui.home.commend.CommendFragment
import com.kaya.basicmodeluitwo.ui.mine.BlankFragment
import kotlinx.android.synthetic.main.layout_bottom_navigation_bar.*
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {

    private var homePageFragment: HomePageFragment? = null

    private var communityFragment: BlankFragment? = null

    private var notificationFragment: HomePageFragment? = null

    private var mineFragment: BlankFragment? = null


    private val fragmentManager: FragmentManager by lazy { supportFragmentManager }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
//        when {
//            messageEvent is SwitchPagesEvent && CommendFragment::class.java == messageEvent.activityClass -> {
//                btnCommunity.performClick()
//            }
//            else -> {
//            }
//        }
    }
    override fun setupViews() {
        setOnClickListener(btnHomePage, btnCommunity, btnNotification, ivRelease, btnMine) {
            when (this) {
                btnHomePage -> {
                    notificationUiRefresh(0)
                    setTabSelection(0)
                }
                btnCommunity -> {
                    notificationUiRefresh(1)
                    setTabSelection(1)
                }
                btnNotification -> {
                    notificationUiRefresh(2)
                    setTabSelection(2)
                }
                ivRelease -> {
                    //LoginActivity.start(this@MainActivity)
                }
                btnMine -> {
                    notificationUiRefresh(3)
                    setTabSelection(3)
                }
            }
        }
        setTabSelection(0)
    }

    private fun notificationUiRefresh(selectionIndex: Int) {
        when (selectionIndex) {
            0 -> {
                if (ivHomePage.isSelected) EventBus.getDefault().post(RefreshEvent(HomePageFragment::class.java))
            }
            1 -> {
                if (ivCommunity.isSelected) EventBus.getDefault().post(RefreshEvent(HomePageFragment::class.java))
            }
            2 -> {
                if (ivNotification.isSelected) EventBus.getDefault().post(RefreshEvent(HomePageFragment::class.java))
            }
            3 -> {
                if (ivMine.isSelected) EventBus.getDefault().post(RefreshEvent(HomePageFragment::class.java))
            }
        }
    }

    private fun clearAllSelected() {
        ivHomePage.isSelected = false
        tvHomePage.isSelected = false
        ivCommunity.isSelected = false
        tvCommunity.isSelected = false
        ivNotification.isSelected = false
        tvNotification.isSelected = false
        ivMine.isSelected = false
        tvMine.isSelected = false
    }


    private fun hideFragments(transaction: FragmentTransaction) {
        transaction.run {
            if (homePageFragment != null) hide(homePageFragment!!)
            if (communityFragment != null) hide(communityFragment!!)
            if (notificationFragment != null) hide(notificationFragment!!)
            if (mineFragment != null) hide(mineFragment!!)
        }
    }

    private fun setTabSelection(index: Int) {
        clearAllSelected()
        fragmentManager.beginTransaction().apply {
            hideFragments(this)
            when (index) {
                0 -> {
                    ivHomePage.isSelected = true
                    tvHomePage.isSelected = true
                    if (homePageFragment == null) {
                        homePageFragment = HomePageFragment.newInstance()
                        add(R.id.homeActivityFragContainer, homePageFragment!!)
                    } else {
                        show(homePageFragment!!)
                    }
                }
                1 -> {
                    ivCommunity.isSelected = true
                    tvCommunity.isSelected = true
                    if (communityFragment == null) {
                        communityFragment = BlankFragment()
                        add(R.id.homeActivityFragContainer, communityFragment!!)
                    } else {
                        show(communityFragment!!)
                    }
                }
                2 -> {
                    ivNotification.isSelected = true
                    tvNotification.isSelected = true
                    if (notificationFragment == null) {
                        notificationFragment = HomePageFragment()
                        add(R.id.homeActivityFragContainer, notificationFragment!!)
                    } else {
                        show(notificationFragment!!)
                    }
                }
                3 -> {
                    ivMine.isSelected = true
                    tvMine.isSelected = true
                    if (mineFragment == null) {
                        mineFragment = BlankFragment.newInstance()
                        add(R.id.homeActivityFragContainer, mineFragment!!)
                    } else {
                        show(mineFragment!!)
                    }
                }
                else -> {
                    ivHomePage.isSelected = true
                    tvHomePage.isSelected = true
                    if (homePageFragment == null) {
                        homePageFragment = HomePageFragment.newInstance()
                        add(R.id.homeActivityFragContainer, homePageFragment!!)
                    } else {
                        show(homePageFragment!!)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }

}