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
import com.rentreadychecklist.databinding.LivingRoomFormRowBinding
import com.rentreadychecklist.formDataSave.LivingRoomFormItem
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.model.diningRoom.DiningRoomFix
import com.rentreadychecklist.model.diningRoom.DiningRoomOk
import com.rentreadychecklist.model.imageupload.ImageUploadCommon

/**
 * Adapter to show DiningRoom form item in list row.
 */
class DiningRoomFormAdapter(
    private val context: Context,
    private val item: ArrayList<LivingRoomFormItem>,
    private val diningRoomItemList: MutableList<DiningRoom>,
) :
    RecyclerView.Adapter<DiningRoomFormAdapter.ViewHolder>() {

    private val itemContain = arrayListOf(
        context.resources.getString(R.string.itemCarpetAndFlooring),
        context.resources.getString(R.string.itemWallsBaseboardsCeiling),
        context.resources.getString(R.string.itemWindowsExteriorDoors),
        context.resources.getString(R.string.itemDoors),
        context.resources.getString(R.string.itemElectrical),
        context.resources.getString(R.string.itemClosets),
        context.resources.getString(R.string.itemFireplace)
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
                Constants.getItemPosition = bindingAdapterPosition
                Constants.fixPosition = bindingAdapterPosition
                Constants.diningRoomFixList[bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                diningRoomItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                diningRoomItemList[bindingAdapterPosition].ok.ok = ""
                diningRoomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                diningRoomItemList[bindingAdapterPosition].ok = DiningRoomOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formDiningRoom)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE

                diningRoomItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                diningRoomItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                diningRoomItemList[bindingAdapterPosition].fix = DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
                okImagesTitle = item[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.imageFormName = context.resources.getString(R.string.dining_room_ok)
                Constants.okImagePosition = bindingAdapterPosition
            }

            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }

            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                diningRoomItemList[bindingAdapterPosition].ok.ok = ""
                diningRoomItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                diningRoomItemList[bindingAdapterPosition].fix = DiningRoomFix(
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

            if (diningRoomItemList[holder.bindingAdapterPosition].items.ItemCondition1) {
                holder.viewBinding.checkboxA.isChecked = true
            }
            if (diningRoomItemList[holder.bindingAdapterPosition].items.ItemCondition2) {
                holder.viewBinding.checkboxB.isChecked = true
                if (item[position].itemName == context.resources.getString(R.string.itemCarpet)) {
                    holder.viewBinding.ifReplaceSizeTextInputLayout.visibility = View.VISIBLE
                    holder.viewBinding.ifReplaceSizeEditText
                        .setText(diningRoomItemList[holder.bindingAdapterPosition].items.ItemNotes1)
                }
            }



            holder.viewBinding.itemRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.checkboxA -> {
                        holder.viewBinding.ifReplaceSizeTextInputLayout.visibility = View.GONE
                        diningRoomItemList[holder.bindingAdapterPosition].items.ItemCondition1 =
                            true
                        diningRoomItemList[holder.bindingAdapterPosition].items.ItemCondition2 =
                            false
                        if (item[position].itemName == context.resources.getString(R.string.itemCarpet)) {
                            diningRoomItemList[holder.bindingAdapterPosition].items.ItemNotes1 = ""
                        }
                    }
                    R.id.checkboxB -> {
                        diningRoomItemList[holder.bindingAdapterPosition].items.ItemCondition1 =
                            false
                        diningRoomItemList[holder.bindingAdapterPosition].items.ItemCondition2 =
                            true
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
                diningRoomItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes1
            )
            holder.viewBinding.itemEditTextB.setText(
                diningRoomItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes2
            )
            holder.viewBinding.itemEditTextC.setText(
                diningRoomItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes3
            )
            holder.viewBinding.dimensionEditText.setText(
                diningRoomItemList[holder.bindingAdapterPosition]
                    .items.itemNotes4
            )

            holder.viewBinding.itemEditTextA.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    diningRoomItemList[holder.bindingAdapterPosition].items.ItemNotes1 =
                        p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.itemEditTextB.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    diningRoomItemList[holder.bindingAdapterPosition].items.ItemNotes2 =
                        p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.itemEditTextC.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    diningRoomItemList[holder.bindingAdapterPosition].items.ItemNotes3 =
                        p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.dimensionEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    diningRoomItemList[holder.bindingAdapterPosition].items.itemNotes4 =
                        p0.toString()
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
                diningRoomItemList[holder.bindingAdapterPosition].items.ItemNotes1 =
                    p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        if (diningRoomItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (diningRoomItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (diningRoomItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true

        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

}