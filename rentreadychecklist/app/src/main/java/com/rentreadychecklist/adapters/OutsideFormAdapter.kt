package com.rentreadychecklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants.Companion.fixList
import com.rentreadychecklist.constants.Constants.Companion.fixPosition
import com.rentreadychecklist.constants.Constants.Companion.formName
import com.rentreadychecklist.constants.Constants.Companion.getItemPosition
import com.rentreadychecklist.constants.Constants.Companion.imageFormName
import com.rentreadychecklist.constants.Constants.Companion.imageTitle
import com.rentreadychecklist.constants.Constants.Companion.okImagePosition
import com.rentreadychecklist.constants.Constants.Companion.okImagesTitle
import com.rentreadychecklist.databinding.OutsideFormRowBinding
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.outside.OutSideFix
import com.rentreadychecklist.model.outside.Outside
import com.rentreadychecklist.model.outside.OutsideOk


/**
 * Adapter to show Outside form item in list row.
 */
class OutsideFormAdapter(
    private val context: Context,
    private val outdoorItemList: ArrayList<String>,
    private val outsideItemList: MutableList<Outside>,
) : RecyclerView.Adapter<OutsideFormAdapter.ViewHolder>() {



    inner class ViewHolder(val viewBinding: OutsideFormRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE
                imageTitle = outdoorItemList[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace), ""
                )
                getItemPosition = bindingAdapterPosition
                fixPosition = bindingAdapterPosition
                fixList[bindingAdapterPosition].fix = context.resources.getString(R.string.itemFIX)
                outsideItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                outsideItemList[bindingAdapterPosition].ok.ok = ""
                outsideItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                outsideItemList[bindingAdapterPosition].ok = OutsideOk("", imageList)
                formName = context.resources.getString(R.string.formOutside)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                outsideItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                outsideItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                outsideItemList[bindingAdapterPosition].fix = OutSideFix(
                    "", "", "",
                    "", imageList
                )
                okImagePosition = bindingAdapterPosition
                imageFormName = context.resources.getString(R.string.outside_ok)
                okImagesTitle = outdoorItemList[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace), ""
                )
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE

            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }

            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE
                outsideItemList[bindingAdapterPosition].ok.ok = ""
                outsideItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                outsideItemList[bindingAdapterPosition].fix = OutSideFix(
                    "", "", "",
                    "", imageList
                )

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OutsideFormRowBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.viewBinding.formItemText.text = outdoorItemList[position]

        if (outsideItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (outsideItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (outsideItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true
        }

        if (outdoorItemList[holder.bindingAdapterPosition] ==
            context.resources.getString(R.string.itemSatelliteDish)
        ) {
            holder.viewBinding.dishCableStoryLinearLayout.visibility = View.VISIBLE

            if (outsideItemList[holder.bindingAdapterPosition].items.ItemCondition1) {
                holder.viewBinding.checkboxA.isChecked = true
            }

            if (outsideItemList[holder.bindingAdapterPosition].items.ItemCondition2) {
                holder.viewBinding.checkboxB.isChecked = true
            }


            holder.viewBinding.ItemRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.checkboxA -> {
                        outsideItemList[holder.bindingAdapterPosition].items.ItemCondition1 = true
                        outsideItemList[holder.bindingAdapterPosition].items.ItemCondition2 = false
                    }
                    R.id.checkboxB -> {
                        outsideItemList[holder.bindingAdapterPosition].items.ItemCondition1 = false
                        outsideItemList[holder.bindingAdapterPosition].items.ItemCondition2 = true
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return outdoorItemList.size
    }

}