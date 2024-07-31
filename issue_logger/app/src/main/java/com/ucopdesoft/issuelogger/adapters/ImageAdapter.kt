package com.ucopdesoft.issuelogger.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ucopdesoft.issuelogger.databinding.ImageListLayoutBinding
import com.ucopdesoft.issuelogger.listeners.CheckImageListListener

class ImageAdapter(private val list: MutableList<Uri>, private val imageListListener: CheckImageListListener) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ImageListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            Glide.with(binding.uploadedImageIv).load(uri).into(binding.uploadedImageIv)

            binding.imageDeleteIv.setOnClickListener {
                list.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                if (list.isEmpty())
                {
                    imageListListener.checkListImages()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ImageListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("checkList", list.size.toString())
        holder.bind(list[position])
    }
}