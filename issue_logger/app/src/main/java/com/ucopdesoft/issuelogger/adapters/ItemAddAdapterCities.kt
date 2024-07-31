package com.ucopdesoft.issuelogger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ucopdesoft.issuelogger.data.Item
import com.ucopdesoft.issuelogger.databinding.RecyclerItemLayout1Binding
import com.ucopdesoft.issuelogger.databinding.RecyclerItemLayoutBinding

class ItemAddAdapterCities(context: Context, private val itemData: MutableList<Item>):RecyclerView.Adapter<ItemAddAdapterCities.ViewHolder>()
{
    inner class ViewHolder(val binding: RecyclerItemLayout1Binding):RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemLayout1Binding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.img.setImageResource(itemData[position].image)
        holder.binding.date.text = itemData[position].date
        holder.binding.text.text = itemData[position].name
    }
}
