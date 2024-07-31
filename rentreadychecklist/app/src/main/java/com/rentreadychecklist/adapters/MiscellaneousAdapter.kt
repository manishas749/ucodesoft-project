package com.rentreadychecklist.adapters

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.okImagesTitle
import com.rentreadychecklist.databinding.KitchenRowBinding
import com.rentreadychecklist.formDataSave.KitchenFormItem
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.model.miscellaneous.MiscellaneousFix
import com.rentreadychecklist.model.miscellaneous.MiscellaneousOk

/**
 * Adapter to show Miscellaneous form item in list row.
 */
class MiscellaneousAdapter(
    private val context: Context,
    private val item: ArrayList<KitchenFormItem>,
    private val miscellaneousItemList: MutableList<Miscellaneous>
) :
    RecyclerView.Adapter<MiscellaneousAdapter.ViewHolder>() {

    private val itemContain = arrayListOf(
        context.resources.getString(R.string.itemCleaning),
        context.resources.getString(R.string.itemSmokeDetectors),
        context.resources.getString(R.string.itemHAVC),
        context.resources.getString(R.string.itemStairsHallway)
    )

    private val itemContainDetails = arrayListOf(
        context.resources.getString(R.string.itemACFiltersCoverCapsScrews),
        context.resources.getString(R.string.itemFoamAroundCompressor),
    )

    inner class ViewHolder(val viewBinding: KitchenRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE
                Constants.imageTitle = item[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.getItemPosition = bindingAdapterPosition
                Constants.fixPosition = bindingAdapterPosition
                Constants.miscellaneousFixList[bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                miscellaneousItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                miscellaneousItemList[bindingAdapterPosition].ok.ok = ""
                miscellaneousItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                miscellaneousItemList[bindingAdapterPosition].ok = MiscellaneousOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formMiscellaneous)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE
                miscellaneousItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                miscellaneousItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                miscellaneousItemList[bindingAdapterPosition].fix = MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
                okImagesTitle = item[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.okImagePosition = bindingAdapterPosition
                Constants.imageFormName = context.resources.getString(R.string.miscellaneous_ok)
            }
            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }

            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE
                miscellaneousItemList[bindingAdapterPosition].ok.ok = ""
                miscellaneousItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                miscellaneousItemList[bindingAdapterPosition].fix = MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            KitchenRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.viewBinding.formItemText.text = item[position].itemName

        if (itemContain.contains(item[position].itemName)) {
            holder.viewBinding.okNaFixRadioGroup.visibility = View.GONE
            holder.viewBinding.formItemText.typeface = Typeface.DEFAULT_BOLD
        }

        if (itemContainDetails.contains(item[position].itemName)) {
            holder.viewBinding.checkBoxConstraint.visibility = View.VISIBLE
            holder.viewBinding.checkboxA.text = item[position].itemCondition1
            holder.viewBinding.checkboxB.text = item[position].itemCondition2



            if (miscellaneousItemList[holder.bindingAdapterPosition].items.ItemCondition1) {
                holder.viewBinding.checkboxA.isChecked = true
            }
            if (miscellaneousItemList[holder.bindingAdapterPosition].items.ItemCondition2) {
                holder.viewBinding.checkboxB.isChecked = true
            }


            holder.viewBinding.itemRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.checkboxA -> {
                        miscellaneousItemList[holder.bindingAdapterPosition].items.ItemCondition1 =
                            true
                        miscellaneousItemList[holder.bindingAdapterPosition].items.ItemCondition2 =
                            false
                    }
                    R.id.checkboxB -> {
                        miscellaneousItemList[holder.bindingAdapterPosition].items.ItemCondition1 =
                            false
                        miscellaneousItemList[holder.bindingAdapterPosition].items.ItemCondition2 =
                            true

                    }
                }
            }

        }



        if (item[position].itemName == context.resources.getString(R.string.itemBlinds)) {
            holder.viewBinding.itemExtraDetails.visibility = View.VISIBLE


            holder.viewBinding.dimensionEditText.setText(
                miscellaneousItemList[holder.bindingAdapterPosition]
                    .items.itemNotes4
            )

            holder.viewBinding.dimensionEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    miscellaneousItemList[holder.bindingAdapterPosition].items.itemNotes4 =
                        p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }

        if (miscellaneousItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (miscellaneousItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (miscellaneousItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true

        }

    }

    override fun getItemCount(): Int {
        return item.size
    }

}