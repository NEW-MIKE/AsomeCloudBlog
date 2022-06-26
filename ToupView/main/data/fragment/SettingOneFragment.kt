package com.example.astroclient.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.astroclient.App
import com.example.astroclient.R
import com.example.astroclient.databinding.FragmentSettingOneBinding


class SettingOneFragment : BaseFragment<FragmentSettingOneBinding>() {
    override val layoutId: Int
                get() = R.layout.fragment_setting_one

    private var adapterList = ArrayAdapter<String>(App.instance,android.R.layout.simple_spinner_item)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.editOne.input.setOnClickListener {
            // guiderf.findStar()
        }
        adapterList.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        adapterList.insert("0",0)
        adapterList.insert("1",1)
        adapterList.insert("2",2)
        binding.select1.adapter = adapterList
        adapterList.notifyDataSetChanged()
    }

}