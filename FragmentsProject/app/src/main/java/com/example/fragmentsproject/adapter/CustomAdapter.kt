package com.example.fragmentsproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentsproject.databinding.RecyclerItemBinding

class CustomAdapter(var mlist:ArrayList<String>,var context:Context):RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

   inner class ViewHolder internal constructor(var binding: RecyclerItemBinding):RecyclerView.ViewHolder(binding.root) {
       init {





       }


   }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = mlist[position]

    }

    override fun getItemCount(): Int {
        return mlist.size
    }

}





