package com.macgavrina.bluetoothchat

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.macgavrina.bluetoothchat.model.MyBluetoothDevice

class MainViewModel(application: Application) : AndroidViewModel(MainApplication.instance) {

    private val devicesList: MutableLiveData<List<MyBluetoothDevice>> = MutableLiveData()

    fun getDevicesList(): LiveData<List<MyBluetoothDevice>> {
        return devicesList
    }

    fun addDevice(device: MyBluetoothDevice) {
        Log.d("MyApp", "newDevice is added, $device")
        val newDeviceList = devicesList.value?.toMutableList() ?: mutableListOf()
        newDeviceList.add(device)
        devicesList.value = newDeviceList
    }

    fun updatePairedDeviceList(pairedDevicesList: List<MyBluetoothDevice>) {
        devicesList.postValue(pairedDevicesList)
    }
}