package com.example.dmcremalert.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dmcremalert.R
import com.example.dmcremalert.databinding.DeviceRowBinding
import com.example.dmcremalert.model.BluetoothDeviceModel
import com.example.dmcremalert.viewmodel.DeviceListViewModel

class DeviceListAdapter(
    context: Context,
    var list: List<BluetoothDeviceModel>,
    var viewModel: DeviceListViewModel
):RecyclerView.Adapter<DeviceListAdapter.ViewHolder>()
{
    inner class ViewHolder(val viewBinding: DeviceRowBinding):RecyclerView.ViewHolder(viewBinding.root)
    {
        init {
            viewBinding.layout.setOnClickListener {
                viewModel.onBluetoothDeviceClick(list[adapterPosition])
                Navigation.findNavController(viewBinding.root).navigate(R.id.action_deviceListFragment_to_homeFragment)



            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DeviceRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewBinding.device.text = list[position].name
    }

}