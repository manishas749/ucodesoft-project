package com.example.dmcremalert

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PairingRequestReceiver
/**
 * Instantiates a new Pairing request.
 */
    : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothDevice.ACTION_PAIRING_REQUEST) {
            val device1 = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            device1?.setPin("0000".toByteArray())
        }
    }
}