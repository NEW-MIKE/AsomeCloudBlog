package com.example.astroclient.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.astroclient.R
import com.example.astroclient.databinding.ActivitySettingBinding
import com.example.astroclient.fragment.SettingOneFragment
import com.example.astroclient.fragment.TabItem
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.view_tab.view.*

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_setting

    private val fragments = ArrayList<Fragment>()
    private val tabs = arrayOf(
        TabItem("返回", SettingOneFragment::class.java),
        TabItem("场景1", SettingOneFragment::class.java),
        TabItem("场景2", SettingOneFragment::class.java)
    )

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            fragments.clear()
            tabs.forEach {
                supportFragmentManager
                    .getFragment(
                        savedInstanceState,
                        it.fragmentCls.simpleName
                    )?.run {
                        fragments.add(this)
                    }
            }
        } else {
            initFragments()
        }
        initTabLayout()
    }

    /**
     * fragment重叠
     */
    override fun onSaveInstanceState(outState: Bundle) {
        fragments.forEach {
            supportFragmentManager.putFragment(
                outState,
                it.javaClass.simpleName,
                it
            )
        }
        super.onSaveInstanceState(outState)
    }

    private fun initFragments() {
        if (fragments.isEmpty()) {
            tabs.forEach {
                val f = it.fragmentCls.newInstance()
                fragments.add(f)
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEach {
            if (!it.isAdded) transaction.add(
                R.id.fl_content, it, it.javaClass
                    .simpleName
            ).hide(it)
        }
        transaction.commit()
    }

    private fun initTabLayout() {
        binding.tabLayout.run {
            tabs.forEach {
                addTab(newTab().setCustomView(getTabView(it)))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}

                override fun onTabUnselected(p0: TabLayout.Tab?) {}

                override fun onTabSelected(p0: TabLayout.Tab) {
                    if (p0.position == 0) {
                        finish()
                    }
                    initTab(p0.position)
                }
            })
            getTabAt(1)?.select()
        }
        initTab(1)
    }

    private fun getTabView(it: TabItem): View {
        val v = LayoutInflater.from(this).inflate(R.layout.view_tab, null)
        v.tab_name.text = it.name
        return v
    }

    private fun initTab(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (index == position) {
                transaction.show(fragment)
            } else {
                transaction.hide(fragment)
            }
        }
        transaction.commit()
    }

}
