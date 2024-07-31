package com.ucopdesoft.issuelogger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ucopdesoft.issuelogger.databinding.ComplaintDetailImageLayoutBinding

class ComplaintDetailImageAdapter(private val list: List<String>) :
    RecyclerView.Adapter<ComplaintDetailImageAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ComplaintDetailImageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: String) {
            Glide.with(binding.complaintIv).load(imageUri).into(binding.complaintIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ComplaintDetailImageLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}