package com.rentreadychecklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.databinding.OutsideFormRowBinding
import com.rentreadychecklist.model.frontdoors.FrontDoorFix
import com.rentreadychecklist.model.frontdoors.FrontDoorOk
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.model.imageupload.ImageUploadCommon

/**
 * Adapter to show FrontDoor form item in list row.
 */
class FrontDoorFormAdapter(
    private val context: Context,
    private val frontDoorItemList: ArrayList<String>,
    private val frontItemList: MutableList<FrontDoors>,
) : RecyclerView.Adapter<FrontDoorFormAdapter.ViewHolder>() {

    inner class ViewHolder(val viewBinding: OutsideFormRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                Constants.imageTitle = frontDoorItemList[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.getItemPosition = bindingAdapterPosition
                Constants.fixPosition = bindingAdapterPosition
                Constants.frontDoorFixList[bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                frontItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                frontItemList[bindingAdapterPosition].ok.ok = ""
                frontItemList[bindingAdapterPosition].NA = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                frontItemList[bindingAdapterPosition].ok = FrontDoorOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formFrontDoor)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                frontItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                frontItemList[bindingAdapterPosition].NA = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                frontItemList[bindingAdapterPosition].fix = FrontDoorFix(
                    "", "", "",
                    "", imageList
                )
                Constants.okImagePosition = bindingAdapterPosition
                Constants.imageFormName = context.resources.getString(R.string.front_door_ok)
                Constants.okImagesTitle = frontDoorItemList[bindingAdapterPosition].replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE

            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }



            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                frontItemList[bindingAdapterPosition].ok.ok = ""
                frontItemList[bindingAdapterPosition].NA =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                frontItemList[bindingAdapterPosition].fix = FrontDoorFix(
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
        holder.viewBinding.formItemText.text = frontDoorItemList[position]

        if (frontItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (frontItemList[position].NA == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (frontItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true
        }
    }

    override fun getItemCount(): Int {
        return frontDoorItemList.size
    }
}