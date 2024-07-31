package com.ucopdesoft.issuelogger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ucopdesoft.issuelogger.data.Item
import com.ucopdesoft.issuelogger.databinding.RecyclerItemLayoutBinding

class ItemAddAdapter(context: Context, private val itemData: MutableList<Item>):RecyclerView.Adapter<ItemAddAdapter.ViewHolder>()
{
    inner class ViewHolder(val binding: RecyclerItemLayoutBinding):RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.img.setImageResource(itemData[position].image)
        holder.binding.date.text = itemData[position].date
        holder.binding.text.text = itemData[position].name.toString()
    }
}
