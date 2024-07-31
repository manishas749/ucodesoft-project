package com.rentreadychecklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.okImagesTitle
import com.rentreadychecklist.databinding.OutsideFormRowBinding
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.model.laundry.LaundryRoomFix
import com.rentreadychecklist.model.laundry.LaundryRoomOk

/**
 * Adapter to show Laundry form item in list row.
 */
class LaundryFromAdapter(
    val context: Context,
    private val laundryItem: ArrayList<String>,
    private val laundryRoomItemList: MutableList<LaundryRoom>

) : RecyclerView.Adapter<LaundryFromAdapter.ViewHolder>() {

    inner class ViewHolder(val viewBinding: OutsideFormRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                Constants.imageTitle = laundryItem[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.getItemPosition = bindingAdapterPosition
                Constants.fixPosition = bindingAdapterPosition
                Constants.laundryFixList[bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                laundryRoomItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                laundryRoomItemList[bindingAdapterPosition].ok.ok = ""
                laundryRoomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                laundryRoomItemList[bindingAdapterPosition].ok = LaundryRoomOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formLaundry)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE
                laundryRoomItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                laundryRoomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                laundryRoomItemList[bindingAdapterPosition].fix = LaundryRoomFix(
                    "", "",
                    "", "", imageList
                )
                okImagesTitle = laundryItem[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.okImagePosition = bindingAdapterPosition
                Constants.imageFormName = context.resources.getString(R.string.laundry_room_ok)
            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }


            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                laundryRoomItemList[bindingAdapterPosition].ok.ok = ""
                laundryRoomItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                laundryRoomItemList[bindingAdapterPosition].fix = LaundryRoomFix(
                    "", "",
                    "", "", imageList
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OutsideFormRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewBinding.formItemText.text = laundryItem[position]

        if (laundryRoomItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (laundryRoomItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (laundryRoomItemList[position].fix.fix ==
            context.resources.getString(R.string.itemFIX)
        ) {
            holder.viewBinding.fix.isChecked = true
        }
    }

    override fun getItemCount(): Int {
        return laundryItem.size
    }
}