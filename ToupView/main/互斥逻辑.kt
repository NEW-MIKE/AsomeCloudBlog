
    fun guideDevice1() {
        guideAdapterList.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.guidingDevice.select.adapter = guideAdapterList
        binding.guidingDevice.connect.setOnCheckedChangeListener { _, isCheck ->
            GlobalScope.launch {
                try {
                    if (isCheck) {
                        guideDevice?.let {
                            withContext(Dispatchers.Main) {
                                if (mainIndex != -1){
                                    var mainList = ArrayList<String>(listDevices)
                                    if(guideIndex != -1)
                                    mainList.remove(listDevices.get(guideIndex))
                                    mainAdapterList.apply {
                                        clear()
                                        addAll(mainList)
                                        notifyDataSetChanged()
                                        binding.mainDevice.select.setSelection(mainList.indexOf(mainDevice))
                                    }
                                }
                                binding.guidingDevice.select.isEnabled = false
                            }
                            withContext(Dispatchers.IO) {
                                guider.select_camera("guide", guideDevice)
                                guider.connectGuide()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            if(guideIndex != -1){
                                mainAdapterList.apply {
                                    clear()
                                    addAll(listDevices)
                                    notifyDataSetChanged()
                                    binding.mainDevice.select.setSelection(listDevices.indexOf(mainDevice))
                                }
                                withContext(Dispatchers.IO) {
                                    guider.disconnectGuide()
                                }
                            }
                            binding.guidingDevice.select.isEnabled = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.guidingDevice.select.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    guideIndex = listDevices.indexOf(guideAdapterList.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }
	
	
	
	
    val listDevices = ArrayList<String>()
    var mainIndex = -1
    var guideIndex = -1
    fun updateDevice1() {
        binding.popuwindown.selectedCCDs.isChecked = true
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                recoveryDeviceStatus()
                listDevices.addAll(guider.devicesCurrentValid)
                mainIndex = listDevices.indexOf(mainDevice)
                guideIndex = listDevices.indexOf(guideDevice)

                if (mainIndex == -1) {
                    mainAdapterList.apply {
                        clear()
                        notifyDataSetChanged()
                    }
                } else {
                    val mainList = ArrayList<String>(listDevices)
                    if ((guideIndex != -1) && binding.guidingDevice.connect.isChecked) {
                        mainList.remove(listDevices.get(guideIndex))
                    }
                    mainAdapterList.apply {
                        clear()
                        addAll(mainList)
                        notifyDataSetChanged()
                    }
                    binding.mainDevice.select.setSelection(mainList.indexOf(mainDevice))
                }

                if (guideIndex == -1) {
                    guideAdapterList.apply {
                        clear()
                        notifyDataSetChanged()
                    }
                } else {
                    val guideList = ArrayList<String>(listDevices)
                    if ((mainIndex != -1) && binding.mainDevice.connect.isChecked) {
                        guideList.remove(listDevices.get(mainIndex))
                    }
                    guideAdapterList.apply {
                        clear()
                        addAll(guideList)
                        notifyDataSetChanged()
                    }
                    binding.guidingDevice.select.setSelection(guideList.indexOf(guideDevice))
                }

            }
        }
    }

	
	
	
	