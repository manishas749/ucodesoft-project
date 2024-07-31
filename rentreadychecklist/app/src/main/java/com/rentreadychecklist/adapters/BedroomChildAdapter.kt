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
import com.rentreadychecklist.constants.Constants.Companion.bedroomFixList
import com.rentreadychecklist.constants.Constants.Companion.bedroomFixPosition
import com.rentreadychecklist.constants.Constants.Companion.fixPosition
import com.rentreadychecklist.constants.Constants.Companion.imageFormName
import com.rentreadychecklist.constants.Constants.Companion.okImageBedroomPosition
import com.rentreadychecklist.constants.Constants.Companion.okImagePosition
import com.rentreadychecklist.constants.Constants.Companion.okImagesTitle
import com.rentreadychecklist.databinding.LivingRoomFormRowBinding
import com.rentreadychecklist.formDataSave.LivingRoomFormItem
import com.rentreadychecklist.model.bedrooms.BedroomFix
import com.rentreadychecklist.model.bedrooms.BedroomOk
import com.rentreadychecklist.model.bedrooms.Bedrooms
import com.rentreadychecklist.model.imageupload.ImageUploadCommon


/**
 * Adapter to show Bedroom form item in list row.
 */
class BedroomChildAdapter(
    private val context: Context,
    private val item: ArrayList<LivingRoomFormItem>,
    private val bedroomItemList: MutableList<Bedrooms>,
    private val bedroomPosition: Int,
) : RecyclerView.Adapter<BedroomChildAdapter.ViewHolder>() {

    private val itemContain = arrayListOf(
        context.resources.getString(R.string.itemCarpetAndFlooring),
        context.resources.getString(R.string.itemWallsBaseboardsCeiling),
        context.resources.getString(R.string.itemWindowsSlidingGlassDoors),
        context.resources.getString(R.string.itemDoors),
        context.resources.getString(R.string.itemElectrical),
        context.resources.getString(R.string.itemClosets)
    )

    private val itemContainDetails = arrayListOf(
        context.resources.getString(R.string.itemCarpet),
        context.resources.getString(R.string.itemPaint),
        context.resources.getString(R.string.itemEvidenceOfWaterDamage)
    )

    inner class ViewHolder(val viewBinding: LivingRoomFormRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            viewBinding.fix.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE
                Constants.imageTitle = item[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                bedroomFixPosition = bedroomPosition
                fixPosition = bindingAdapterPosition
                bedroomFixList[bedroomPosition][bindingAdapterPosition].fix = context.resources
                    .getString(R.string.itemFIX)
                Constants.getItemPosition = bindingAdapterPosition
                bedroomItemList[bindingAdapterPosition].fix.fix = context.resources
                    .getString(R.string.itemFIX)
                bedroomItemList[bindingAdapterPosition].ok.ok = ""
                bedroomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                bedroomItemList[bindingAdapterPosition].ok = BedroomOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formBedroom)
                Constants.bedroomPosition = bedroomPosition
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE
                bedroomItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                bedroomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                bedroomItemList[bindingAdapterPosition].fix = BedroomFix(
                    "", "", "",
                    "", imageList
                )
                okImagePosition = bindingAdapterPosition
                okImagesTitle = item[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                imageFormName = context.resources.getString(R.string.bedroom_ok)
                okImageBedroomPosition = bedroomPosition
            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }

            viewBinding.na.setOnClickListener {

                viewBinding.uploadOkImageButton.visibility = View.GONE
                bedroomItemList[bindingAdapterPosition].ok.ok = ""
                bedroomItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                bedroomItemList[bindingAdapterPosition].fix = BedroomFix(
                    "", "", "",
                    "", imageList
                )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LivingRoomFormRowBinding.inflate(
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

            if (bedroomItemList[holder.bindingAdapterPosition].items.ItemCondition1) {
                holder.viewBinding.checkboxA.isChecked = true
            }
            if (bedroomItemList[holder.bindingAdapterPosition].items.ItemCondition2) {
                holder.viewBinding.checkboxB.isChecked = true
                if (item[position].itemName == context.resources.getString(R.string.itemCarpet)) {
                    holder.viewBinding.ifReplaceSizeTextInputLayout.visibility = View.VISIBLE
                    holder.viewBinding.ifReplaceSizeEditText
                        .setText(bedroomItemList[holder.bindingAdapterPosition].items.ItemNotes1)
                }
            }



            holder.viewBinding.itemRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.checkboxA -> {
                        holder.viewBinding.ifReplaceSizeTextInputLayout.visibility = View.GONE
                        bedroomItemList[holder.bindingAdapterPosition].items.ItemCondition1 = true
                        bedroomItemList[holder.bindingAdapterPosition].items.ItemCondition2 = false
                        if (item[position].itemName == context.resources.getString(R.string.itemCarpet)) {
                            bedroomItemList[holder.bindingAdapterPosition].items.ItemNotes1 = ""
                        }
                    }
                    R.id.checkboxB -> {
                        bedroomItemList[holder.bindingAdapterPosition].items.ItemCondition1 = false
                        bedroomItemList[holder.bindingAdapterPosition].items.ItemCondition2 = true
                        if (item[position].itemName == context.resources.getString(R.string.itemCarpet)) {
                            holder.viewBinding.ifReplaceSizeTextInputLayout.visibility =
                                View.VISIBLE
                        }
                    }
                }
            }
        }


        if (item[position].itemName == context.resources.getString(R.string.itemBlinds)) {
            holder.viewBinding.itemExtraDetails.visibility = View.VISIBLE

            holder.viewBinding.itemEditTextA.setText(
                bedroomItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes1
            )
            holder.viewBinding.itemEditTextB.setText(
                bedroomItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes2
            )
            holder.viewBinding.itemEditTextC.setText(
                bedroomItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes3
            )
            holder.viewBinding.dimensionEditText.setText(
                bedroomItemList[holder.bindingAdapterPosition]
                    .items.itemNotes4
            )

            holder.viewBinding.itemEditTextA.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bedroomItemList[holder.bindingAdapterPosition].items.ItemNotes1 = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.itemEditTextB.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bedroomItemList[holder.bindingAdapterPosition].items.ItemNotes2 = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.itemEditTextC.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bedroomItemList[holder.bindingAdapterPosition].items.ItemNotes3 = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.dimensionEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bedroomItemList[holder.bindingAdapterPosition].items.itemNotes4 = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }

        holder.viewBinding.ifReplaceSizeEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                bedroomItemList[holder.bindingAdapterPosition].items.ItemNotes1 =
                    p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        if (bedroomItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (bedroomItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (bedroomItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true

        }
    }

    override fun getItemCount(): Int {
        return bedroomItemList.size
    }
}