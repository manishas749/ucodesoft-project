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
import com.rentreadychecklist.formDataSave.KitchenFormItem
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.model.kitchen.KitchenFix
import com.rentreadychecklist.model.kitchen.KitchenOk

/**
 * Adapter to show Kitchen form item in list row.
 */
class KitchenFormAdapter(
    private val context: Context,
    private val item: ArrayList<KitchenFormItem>,
    private val kitchenItemList: MutableList<Kitchen>
) :
    RecyclerView.Adapter<KitchenFormAdapter.ViewHolder>() {

    private val itemContain = arrayListOf(
        context.resources.getString(R.string.itemSink),
        context.resources.getString(R.string.itemAppliances),
        context.resources.getString(R.string.itemElectrical),
        context.resources.getString(R.string.itemPantry),
        context.resources.getString(R.string.itemWindows)
    )

    private val itemContainDetails = arrayListOf(
        context.resources.getString(R.string.itemRefrigerator),
        context.resources.getString(R.string.itemDishwasher)
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
                Constants.kitchenFixList[bindingAdapterPosition].fix =
                    context.resources.getString(R.string.itemFIX)
                kitchenItemList[bindingAdapterPosition].fix.fix =
                    context.resources.getString(R.string.itemFIX)
                kitchenItemList[bindingAdapterPosition].ok.ok = ""
                kitchenItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                kitchenItemList[bindingAdapterPosition].ok = KitchenOk("", imageList)
                Constants.formName = context.resources.getString(R.string.formKitchen)
                Navigation.findNavController(viewBinding.root).navigate(R.id.fixModelSheetFragment)
            }

            viewBinding.ok.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.VISIBLE

                kitchenItemList[bindingAdapterPosition].ok.ok =
                    context.resources.getString(R.string.itemOK)
                kitchenItemList[bindingAdapterPosition].na = ""
                val imageList = arrayListOf<ImageUploadCommon>()
                kitchenItemList[bindingAdapterPosition].fix = KitchenFix(
                    "", "", "",
                    "", imageList
                )
                okImagesTitle = item[bindingAdapterPosition].itemName.replace(
                    context.resources
                        .getString(R.string.oldReplace),
                    ""
                )
                Constants.okImagePosition = bindingAdapterPosition
                Constants.imageFormName = context.resources.getString(R.string.kitchen_ok)

            }
            viewBinding.uploadOkImageButton.setOnClickListener {
                Navigation.findNavController(viewBinding.root).navigate(R.id.okModelSheetFragment)
            }

            viewBinding.na.setOnClickListener {
                viewBinding.uploadOkImageButton.visibility = View.GONE

                kitchenItemList[bindingAdapterPosition].ok.ok = ""
                kitchenItemList[bindingAdapterPosition].na =
                    context.resources.getString(R.string.itemNA)
                val imageList = arrayListOf<ImageUploadCommon>()
                kitchenItemList[bindingAdapterPosition].fix = KitchenFix(
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

            if (item[position].itemName == context.resources.getString(R.string.itemDishwasher)) {
                holder.viewBinding.checkboxB.visibility = View.GONE
            }

            if (kitchenItemList[holder.bindingAdapterPosition].items.ItemCondition1) {
                holder.viewBinding.checkboxA.isChecked = true
            }
            if (kitchenItemList[holder.bindingAdapterPosition].items.ItemCondition2) {
                holder.viewBinding.checkboxB.isChecked = true
            }

            holder.viewBinding.itemRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.checkboxA -> {
                        kitchenItemList[holder.bindingAdapterPosition].items.ItemCondition1 = true
                        kitchenItemList[holder.bindingAdapterPosition].items.ItemCondition2 = false
                    }
                    R.id.checkboxB -> {
                        kitchenItemList[holder.bindingAdapterPosition].items.ItemCondition1 = false
                        kitchenItemList[holder.bindingAdapterPosition].items.ItemCondition2 = true

                    }
                }
            }
        }

        if (item[position].itemName == context.resources.getString(R.string.itemBlinds)) {
            holder.viewBinding.itemExtraDetails.visibility = View.VISIBLE
            holder.viewBinding.itemEditTextA.setText(
                kitchenItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes1
            )
            holder.viewBinding.itemEditTextB.setText(
                kitchenItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes2
            )
            holder.viewBinding.itemEditTextC.setText(
                kitchenItemList[holder.bindingAdapterPosition]
                    .items.ItemNotes3
            )
            holder.viewBinding.dimensionEditText.setText(
                kitchenItemList[holder.bindingAdapterPosition]
                    .items.itemNotes4
            )

            holder.viewBinding.itemEditTextA.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //todo
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    kitchenItemList[holder.bindingAdapterPosition].items.ItemNotes1 =
                        p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                    //todo
                }

            })

            holder.viewBinding.itemEditTextB.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    kitchenItemList[holder.bindingAdapterPosition].items.ItemNotes2 =
                        p0.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            holder.viewBinding.itemEditTextC.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    kitchenItemList[holder.bindingAdapterPosition].items.ItemNotes3 =
                        p0.toString()
                            .trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })


            holder.viewBinding.dimensionEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //todo
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    kitchenItemList[holder.bindingAdapterPosition].items.itemNotes4 =
                        p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                    //todo
                }

            })
        }

        if (kitchenItemList[position].ok.ok == context.resources.getString(R.string.itemOK)) {
            holder.viewBinding.ok.isChecked = true
        }

        if (kitchenItemList[position].na == context.resources.getString(R.string.itemNA)) {
            holder.viewBinding.na.isChecked = true
        }

        if (kitchenItemList[position].fix.fix == context.resources.getString(R.string.itemFIX)) {
            holder.viewBinding.fix.isChecked = true

        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

}