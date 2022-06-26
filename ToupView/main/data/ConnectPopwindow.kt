package com.example.astroclient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.astroclient.databinding.SettingConnectBinding
import com.example.astroclient.util.Guider
import com.example.astroclient.util.Profile
import com.example.astroclient.util.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectPopwindow(val context: Context) : PopupWindow() {
    private val TAG: String = "ConnectPopwindow"
    val inflate: SettingConnectBinding

    var mMainActivityContext = context

    init {
        inflate = SettingConnectBinding.inflate(LayoutInflater.from(context))
        contentView = inflate.root
        isFocusable = true
        init()
    }

    var opened: Boolean? = false
    var hostAddr = "192.168.1.170" //192.168.1.31  10.0.0.121
    var guider: Guider = Guider(hostAddr)
    var profile: Profile? = null
    var choosenCCD: String? = null
    private var adapter = MySpinnerAdapter(context)
    private var adapterCCDs = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var adapterScope = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
    private var adapterFW = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)

    fun init() {
        inflate.edittextHost.addTextChangedListener(afterTextChanged = {
            guider.setHostAddr(it.toString())
        })

        disableView()
        inflate.connect.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                try {
                    if (isCheck) {
                        guider.connect()
                        withContext(Dispatchers.Main) {
                            inflate.selectCCDs.isEnabled = true
                            inflate.selectedCCDs.isEnabled = true
                            inflate.selectFW.isEnabled = true
                            inflate.selectedFW.isEnabled = true
                            inflate.selectScope.isEnabled = true
                            inflate.selectedScope.isEnabled = true
                            loadEquipment()
                        }
                    } else {
                        guider.disconnect()
                        withContext(Dispatchers.Main) {
                            disableView()
                        }
                        opened = false

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        disableView()
                        Toast.makeText(App.instance, "网络异常", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        inflate.selectedCCDs.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                if (isCheck) {
                    choosenCCD?.run {
                        guider.startDriver(choosenCCD!!)
                    }
                } else {
                    choosenCCD?.run {
                        guider.stopDriver(choosenCCD!!)
                    }
                }

            }
        }

        inflate.selectCCDs.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    choosenCCD = adapterCCDs.getItem(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        inflate.selectedScope.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                if (isCheck) {
                } else {
                }
            }
        }

        inflate.selectScope.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }


        inflate.selectedFW.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                if (isCheck) {
                } else {
                }
            }
        }

        inflate.selectFW.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun disableView() {
        inflate.selectCCDs.isEnabled = false
        inflate.selectedCCDs.isEnabled = false
        inflate.selectFW.isEnabled = false
        inflate.selectedFW.isEnabled = false
        inflate.selectScope.isEnabled = false
        inflate.selectedScope.isEnabled = false
    }


    suspend fun loadEquipment() {

        var ccds = withContext(Dispatchers.IO) { guider.getEquipmentCCDs() }
        adapterCCDs.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        for (i in 0 until ccds.size)
            adapterCCDs.insert(ccds.get(i), i)
        inflate.selectCCDs.adapter = adapterCCDs
        adapterCCDs.notifyDataSetChanged()
        for (i in 0 until ccds.size) {
            if ("ToupCam" == ccds.get(i)) {
                inflate.selectCCDs.setSelection(i)
                break
            }
        }

        var scopes = withContext(Dispatchers.IO) { guider.getEquipmentScopes() }
        adapterScope.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        for (i in 0 until scopes.size)
            adapterScope.insert(scopes.get(i), i)
        inflate.selectScope.adapter = adapterScope
        adapterScope.notifyDataSetChanged()

        var fws = withContext(Dispatchers.IO) { guider.getEquipmentFWs() }
        adapterFW.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        for (i in 0 until fws.size)
            adapterFW.insert(fws.get(i), i)
        inflate.selectFW.adapter = adapterFW
        adapterFW.notifyDataSetChanged()
    }

    public fun updateUi() {
        inflate.edittextHost.setText(hostAddr)
    }

    private class MySpinnerAdapter(val context: Context) : BaseAdapter() {
        private val TAG = "MySpinnerAdapter"
        var mList = ArrayList<Profile>()

        override fun getCount(): Int {
            return mList.size
        }

        override fun getItem(position: Int): Profile {
            return mList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var textView: TextView =
                (convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.simple_spinner_item, parent, false)) as TextView
            var item = getItem(position)
            textView.setText(item.name)
            return textView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var textView: TextView = convertView?.run { this as TextView } ?: TextView(context)
            var item = getItem(position)
            textView.setText(item.name)
            return textView
        }
    }

}
