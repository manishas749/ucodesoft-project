package com.rentreadychecklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentreadychecklist.R
import com.rentreadychecklist.RoomUpdateInterface
import com.rentreadychecklist.model.imageupload.ImageUploadCommon

/**
 * Adapter to show images in row using Glide.
 */
class ImageAdapter(
    private val list: MutableList<ImageUploadCommon>,
    private val context: Context,
    private val imageListCheckListener: RoomUpdateInterface
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.findViewById<ImageView>(R.id.image)!!
        private var deleteImage = view.findViewById<ImageView>(R.id.deleteImage)!!

        init {
            deleteImage.setOnClickListener {
                list.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
                if (list.isEmpty()) {
                    imageListCheckListener.checkImageList()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.image_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Glide.with(context).load(list[holder.bindingAdapterPosition].imagePath)
                .into(holder.image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}