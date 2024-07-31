package com.rentreadychecklist.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.okImagePosition
import com.rentreadychecklist.databinding.OutsideFormRowBinding
import com.rentreadychecklist.model.bathroom.BathroomFix
import com.rentreadychecklist.model.bathroom.BathroomOk
import com.rentreadychecklist.model.bathroom.Bathrooms
import com.rentreadychecklist.model.imageupload.ImageUploadCommon

/**
 * Adapter to show Bathroom form item in list row.
 */
class BathroomChildAdapter(
    private val context: Context,
    private val item: ArrayList<String>,
    private val bathroomItemList: MutableList<Bathrooms>,
    private val bathroomPosition: Int
) : RecyclerView.Adapter<BathroomChildAdapter.ViewHolder>() {

    private val itemContain = arrayListOf(
        context.resources.getString(R.string.itemShowerTub),
        context.resources.getString(R.string.itemSink),
        context.resources.getString(R.string.itemToilet),
        context.resources.getString(R.string.itemDoors)
    )

    inner class ViewHolder(val viewBinding: OutsideFormRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE
                Constants.imageTitle = item[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace), ""
                )
                Constants.bathroomFixPosition = bathroomPosition
                Constants.fixPosition = bindingAdapterPosition
                Constants.bathroomFixList[bathroomPosition][bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                Constants.getItemPosition = bindingAdapterPosition
                bathroomItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                bathroomItemList[bindingAdapterPosition].ok.ok = ""
                bathroomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                bathroomItemList[bindingAdapterPosition].ok = BathroomOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formBathroom)
                Constants.bathroomPosition = bathroomPosition
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE
                bathroomItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                bathroomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                bathroomItemList[bindingAdapterPosition].fix = BathroomFix(
                    "", "", "",
                    "", imageList
                )
                okImagePosition = bindingAdapterPosition
                Constants.okImagesTitle = item[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.imageFormName = context.resources.getString(R.string.bathroom_ok)
                Constants.okImageBathroomPosition = bathroomPosition

            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }

            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                bathroomItemList[bindingAdapterPosition].ok.ok = ""
                bathroomItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                bathroomItemList[bindingAdapterPosition].fix = BathroomFix(
                    "", "", "",
                    "", imageList
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

        holder.viewBinding.formItemText.text = item[position]

        if (itemContain.contains(item[position])) {
            holder.viewBinding.okNaFixRadioGroup.visibility = View.GONE
            holder.viewBinding.formItemText.typeface = Typeface.DEFAULT_BOLD
        }

        if (bathroomItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (bathroomItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (bathroomItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true
        }

    }

    override fun getItemCount(): Int {
        return bathroomItemList.size
    }
}