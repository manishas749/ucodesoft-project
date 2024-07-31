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
import com.rentreadychecklist.constants.Constants.Companion.imageFormName
import com.rentreadychecklist.constants.Constants.Companion.okImagePosition
import com.rentreadychecklist.databinding.OutsideFormRowBinding
import com.rentreadychecklist.formDataSave.GarageFormItem
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.model.garage.GarageDoorsOk
import com.rentreadychecklist.model.garage.GarageFix
import com.rentreadychecklist.model.imageupload.ImageUploadCommon

/**
 * Adapter to show Garage form item in list row.
 */
class GarageFormAdapter(
    private val context: Context,
    private val garageItemList: ArrayList<GarageFormItem>,
    private val garageFormItemList: MutableList<Garage>,
) : RecyclerView.Adapter<GarageFormAdapter.ViewHolder>() {

    private val itemContain = arrayListOf(
        context.resources.getString(R.string.itemElectrical),
        context.resources.getString(R.string.itemWaterHeater),
        context.resources.getString(R.string.itemWallsBaseboardsCeiling)
    )

    private val itemContainDetails = arrayListOf(
        context.resources.getString(R.string.itemPaint),
    )

    inner class ViewHolder(val viewBinding: OutsideFormRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                Constants.imageTitle = garageItemList[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.getItemPosition = bindingAdapterPosition
                Constants.fixPosition = bindingAdapterPosition
                Constants.garageFixList[bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                garageFormItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                garageFormItemList[bindingAdapterPosition].ok.ok = ""
                garageFormItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                garageFormItemList[bindingAdapterPosition].ok = GarageDoorsOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formGarage)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE
                garageFormItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                garageFormItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                garageFormItemList[bindingAdapterPosition].fix = GarageFix(
                    "", "", "",
                    "", imageList
                )
                Constants.okImagesTitle = garageItemList[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                imageFormName = context.resources.getString(R.string.garage_ok)
                okImagePosition = bindingAdapterPosition
            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }


            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE


                garageFormItemList[bindingAdapterPosition].ok.ok = ""
                garageFormItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                garageFormItemList[bindingAdapterPosition].fix = GarageFix(
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
        holder.viewBinding.formItemText.text = garageItemList[position].itemName

        if (itemContain.contains(garageItemList[position].itemName)) {
            holder.viewBinding.okNaFixRadioGroup.visibility = View.GONE
            holder.viewBinding.formItemText.typeface = Typeface.DEFAULT_BOLD
        }

        if (garageFormItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (garageFormItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (garageFormItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true
        }

        if (itemContainDetails.contains(garageItemList[position].itemName)) {
            holder.viewBinding.checkBoxConstraint.visibility = View.VISIBLE
            holder.viewBinding.checkbox1.text = garageItemList[position].itemCondition1
            holder.viewBinding.checkbox2.text = garageItemList[position].itemCondition2

            if (garageFormItemList[holder.bindingAdapterPosition].items.ItemCondition1) {
                holder.viewBinding.checkbox1.isChecked = true
            }
            if (garageFormItemList[holder.bindingAdapterPosition].items.ItemCondition2) {
                holder.viewBinding.checkbox2.isChecked = true
            }
        }

        holder.viewBinding.itemRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.checkbox1 -> {
                    garageFormItemList[holder.bindingAdapterPosition].items.ItemCondition1 = true
                    garageFormItemList[holder.bindingAdapterPosition].items.ItemCondition2 = false

                }

                R.id.checkbox2 -> {
                    garageFormItemList[holder.bindingAdapterPosition].items.ItemCondition1 = false
                    garageFormItemList[holder.bindingAdapterPosition].items.ItemCondition2 = true

                }
            }
        }
    }


    override fun getItemCount(): Int {
        return garageItemList.size
    }

}