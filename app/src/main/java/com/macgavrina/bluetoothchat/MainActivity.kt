package com.macgavrina.bluetoothchat

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.macgavrina.bluetoothchat.constants.PERMISSIONS_REQUEST_LOCATION
import com.macgavrina.bluetoothchat.model.MyBluetoothDevice
import com.macgavrina.bluetoothchat.DeviceRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DeviceRecyclerViewAdapter.OnDeviceInListClickListener {

    override fun onDeviceClick(device: MyBluetoothDevice) {
        //ToDo handle on device click
        Log.d("MyApp", "onClick device = $device")
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var viewModel: MainViewModel

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    Log.d("MyApp", "not-paired deviceName = $deviceName")
                    val bluetoothDevice = MyBluetoothDevice(deviceName, deviceHardwareAddress, false)
                    viewModel.addDevice(bluetoothDevice)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        requestPermissionsForBluetooth()

        setupBluetooth()

        setupUI()

        registerBroadcastReceiverForBluetoothScan()

        scanForPairedDevices()
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    private fun requestPermissionsForBluetooth() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //ToDo Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSIONS_REQUEST_LOCATION)
                //ToDo - check result
            }
        } else {
            // Permission has already been granted
        }

    }

    private fun setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            //ToDo Device doesn't support Bluetooth, disable all and notify user
        }
    }

    private fun setupUI() {
        main_activity_scan_button.setOnClickListener {
            Log.d("MyApp", "Scan button is pressed")
            bluetoothAdapter?.startDiscovery()
            //ToDo change button to "stop scan" to disable scanning
        }

        val adapter = DeviceRecyclerViewAdapter(this)
        main_activity_devices_list.adapter = adapter
        main_activity_devices_list.layoutManager = LinearLayoutManager(MainApplication.applicationContext())

        viewModel.getDevicesList().observe(this, Observer { devicesList ->

            adapter.setBluetoothDevices(devicesList)
            //ToDo display devices list in recycler view
            Log.d("MyApp", "Devices list is changed, print it")
            devicesList.forEach {device ->
                Log.d("MyApp", device.toString())
            }
        })
    }

    private fun scanForPairedDevices() {
        //ToDo it have to be in viewModel, or in app, not in Activity
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val pairedDevicesList = mutableListOf<MyBluetoothDevice>()
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            Log.d("MyApp", "paired deviceName = $deviceName")
            val device = MyBluetoothDevice(deviceName, deviceHardwareAddress, true)
            pairedDevicesList.add(device)
        }
        viewModel.updatePairedDeviceList(pairedDevicesList)
        //ToDo cancelDiscovery() after picking one of devices or cancelling scan or pausing activity
    }

    private fun registerBroadcastReceiverForBluetoothScan() {

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }
}
