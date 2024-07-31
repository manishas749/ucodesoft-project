package com.example.dmcremalert.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.dmcremalert.model.BluetoothDeviceModel
import com.example.dmcremalert.preference.SharedPreference

class DeviceListViewModel(private var application: Application) : AndroidViewModel(application) {

    private var bluetoothDeviceList: ArrayList<BluetoothDeviceModel> = arrayListOf()
    val bluetoothDeviceLiveData = MutableLiveData<List<BluetoothDeviceModel>>()
    private val sharedPreference = SharedPreference(application)

    private var mBluetoothAdapter: BluetoothAdapter? = null


    fun startScanning() {
        bluetoothDeviceList = ArrayList<BluetoothDeviceModel>()

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null) {
            searchDevices()
        } else {
            Log.d("devices", "devices not available")
        }
    }

    @SuppressLint("MissingPermission")
    fun searchDevices() {
        val isBluetoothEnabled = mBluetoothAdapter!!.isEnabled
        if (isBluetoothEnabled) {
            if (mBluetoothAdapter!!.isDiscovering) {
                mBluetoothAdapter!!.cancelDiscovery()
            }
            mBluetoothAdapter!!.startDiscovery()

        } else {
            BluetoothAdapter.getDefaultAdapter().isEnabled
        }
        val filter = IntentFilter()
        filter.addAction(if (isBluetoothEnabled) BluetoothDevice.ACTION_FOUND else BluetoothAdapter.ACTION_STATE_CHANGED)
        application.registerReceiver(mReceiver, filter)
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device =
                            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                                ?: return
                        var exist = false
                        for (devices in bluetoothDeviceList) {
                            if (devices.address == device.address) {
                                exist = true
                                break
                            }
                        }
                        if (!exist && device.name != null && device.name.isNotEmpty()) {
                            val bluetoothDevice = BluetoothDeviceModel(device.name, device.address)
                            bluetoothDeviceList.add(bluetoothDevice)

                        }
                        bluetoothDeviceLiveData.value = bluetoothDeviceList


                    }
                    BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                        val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                        val previousState =
                            intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)
                        Log.d(
                            "ACTION_BOND_STATE_CHNG",
                            "ACTION_BOND_STATE_CHANGED: state:$state, previous:$previousState"
                        )
                    }

                    BluetoothDevice.ACTION_PAIRING_REQUEST -> {

                        val device1 =
                            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device1?.setPin("0000".toByteArray())

                    }

                    BluetoothDevice.ACTION_ACL_CONNECTED -> {

                        var bluetoothDevice
                                : BluetoothDevice
                        ? = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                            ?: return
                        if (bluetoothDevice != null) {
                            Log.d(
                                "CONNECTED_deviceName",
                                "" + bluetoothDevice.name + " " + bluetoothDevice.address
                            )
                        }
                        //Navigation

                    }


                    else -> {}
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun unregisterReceiver() {
        try {
            application.unregisterReceiver(mReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        mBluetoothAdapter!!.cancelDiscovery()
    }

    fun onBluetoothDeviceClick(bluetoothDevice: BluetoothDeviceModel) {
        if (bluetoothDevice.name != null)
         {
            connectBluetooth(bluetoothDevice)
        } else {
            Log.d("devices", "devices not available")
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectBluetooth(bluetoothDevice: BluetoothDeviceModel)
    {
        mBluetoothAdapter!!.cancelDiscovery()
        sharedPreference.setSelectedDeviceAddress(bluetoothDevice.address)
        sharedPreference.setSelectedDeviceName(bluetoothDevice.name)
    }
}