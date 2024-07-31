package com.rentreadychecklist.fragments


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentreadychecklist.R
import com.rentreadychecklist.adapters.BathroomChildAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.databinding.FragmentBathroomFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.bathroom.Bathroom
import com.rentreadychecklist.model.bathroom.BathroomFix
import com.rentreadychecklist.model.bathroom.BathroomOk
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save Bathroom Form Data.
 */
class BathroomFormFragment : Fragment() {

    lateinit var viewBinding: FragmentBathroomFormBinding
    lateinit var viewModel: AppViewModel
    private lateinit var bathChildAdapter: BathroomChildAdapter
    private var totalNumberOfBathroom = 0
    private var bathroomNumber = 1
    private val permissionManager = PermissionManager.from(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentBathroomFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataOnAdapter()

        viewBinding.next.setOnClickListener {
            com.rentreadychecklist.utils.Helper.hideKeyBoard(view)
            nextBathRoomPosition()
        }

        viewBinding.editButton.setOnClickListener {
            viewBinding.editButton.visibility = View.GONE
            viewBinding.previous.visibility = View.GONE
            viewBinding.next.visibility = View.GONE
            viewBinding.saveButton.visibility = View.VISIBLE
            viewBinding.bathroomNameEditText.visibility = View.VISIBLE
            viewBinding.formTitle.visibility = View.GONE
            if (FormsDummyData.numberOfBathroom[bathroomNumber - 1].bathroomName != getString(R.string.bathroom)) {
                viewBinding.bathroomNameEditText.setText(FormsDummyData.numberOfBathroom[bathroomNumber - 1].bathroomName)
            } else {
                val bathroomNameNumber = "Bathroom $bathroomNumber"
                viewBinding.bathroomNameEditText.setText(bathroomNameNumber)
            }
            Helper.showKeyBoard(viewBinding.bathroomNameEditText)

        }

        viewBinding.saveButton.setOnClickListener {
            if (viewBinding.bathroomNameEditText.text.isNotEmpty()) {
                viewBinding.saveButton.visibility = View.GONE
                viewBinding.editButton.visibility = View.VISIBLE
                Helper.hideKeyBoard(view)
                saveBathroomName(bathroomNumber)
            } else {
                viewBinding.bathroomNameEditText.error =
                    getString(R.string.please_enter_bathroom_name)
            }

        }

        viewBinding.previous.setOnClickListener {
            com.rentreadychecklist.utils.Helper.hideKeyBoard(view)
            previousBathroom()
        }

    }

    fun editDialog(position: Int) {
        val editDialog = Dialog(requireContext())
        editDialog.setContentView(R.layout.edit_dialog)
        val saveButton = editDialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = editDialog.findViewById<Button>(R.id.cancelButton)
        editDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        editDialog.show()
        var bathroomEditText = editDialog.findViewById<EditText>(R.id.bathroomEditText)
        Helper.showKeyBoard(bathroomEditText)
        if (FormsDummyData.numberOfBathroom[bathroomNumber - 1].bathroomName != getString(R.string.bathroom)) {
            bathroomEditText.setText(FormsDummyData.numberOfBathroom[bathroomNumber - 1].bathroomName)
        } else {
            val bathroomNameNumber = "Bathroom $bathroomNumber"
            bathroomEditText.setText(bathroomNameNumber)
        }

        saveButton.setOnClickListener {
            if (bathroomEditText.text.isNotEmpty()) {
                view?.let { it1 -> Helper.hideKeyBoard(it1) }
                val bathroomName = bathroomEditText.text
                FormsDummyData.numberOfBathroom[position - 1].bathroomName = bathroomName.toString()
                viewBinding.formTitle.text =
                    FormsDummyData.numberOfBathroom[position - 1].bathroomName
                editDialog.dismiss()

            } else {
                bathroomEditText.error =
                    getString(R.string.please_enter_bathroom_name)
            }

        }
        cancelButton.setOnClickListener {
            editDialog.dismiss()
        }


    }

    //set Adapter as per the bathroom Position when clicked on the next button

    private fun nextBathRoomPosition() {
        if (bathroomNumber < totalNumberOfBathroom) {
            when (bathroomNumber) {
                1 -> {
                    onNextClick(1)
                }

                2 -> {
                    onNextClick(2)
                }

                3 -> {
                    onNextClick(3)
                }

                4 -> {
                    onNextClick(4)
                }

                5 -> {
                    onNextClick(5)
                }

                6 -> {
                    if (FormsDummyData.numberOfBathroom.isNotEmpty()) {
                        setDataToFix()
                        setOkImage()
                        viewModel.updateBathroom(FormsDummyData.numberOfBathroom, Constants.FORMID)
                    }
                    try {
                        findNavController().navigate(R.id.action_bathroomFormFragment_to_additionalNotesFragment)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } else {
            if (FormsDummyData.numberOfBathroom.isNotEmpty()) {
                setDataToFix()
                setOkImage()
                viewModel.updateBathroom(FormsDummyData.numberOfBathroom, Constants.FORMID)
            }
            try {
                findNavController().navigate(R.id.action_bathroomFormFragment_to_additionalNotesFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    //set Adapter as per the bathroom Position when clicked on the previous button

    private fun previousBathroom() {

        when (bathroomNumber) {
            1 -> {
                if (FormsDummyData.numberOfBathroom.isNotEmpty()) {
                    setDataToFix()
                    setOkImage()
                    viewModel.updateBathroom(FormsDummyData.numberOfBathroom, Constants.FORMID)
                }
                try {
                    findNavController().navigate(R.id.action_bathroomFormFragment_to_numberOfBathroomsFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            2 -> {
                onPreviousClick(2)
            }

            3 -> {
                onPreviousClick(3)
            }

            4 -> {
                onPreviousClick(4)
            }

            5 -> {
                onPreviousClick(5)
            }

            6 -> {
                onPreviousClick(6)
            }
        }

    }

    private fun onPreviousClick(position: Int) {
        setDataToAdapterWithPosition(position - 2)
        bathroomNumber = position - 1
        if (FormsDummyData.numberOfBathroom[bathroomNumber - 1].bathroomName != getString(R.string.bathroom)) {
            viewBinding.formTitle.text =
                FormsDummyData.numberOfBathroom[bathroomNumber - 1].bathroomName
        } else {
            val bathroomNumberName = "Bathroom $bathroomNumber"
            viewBinding.formTitle.text = bathroomNumberName
        }

    }

    private fun onNextClick(position: Int) {

        if (FormsDummyData.numberOfBathroom.isNotEmpty()) {
            setDataToFix()
            setOkImage()
            viewModel.updateBathroom(FormsDummyData.numberOfBathroom, Constants.FORMID)
        }

        setDataToAdapterWithPosition(position)
        bathroomNumber = position + 1
        if (FormsDummyData.numberOfBathroom[position].bathroomName != getString(R.string.bathroom)) {
            viewBinding.formTitle.text = FormsDummyData.numberOfBathroom[position].bathroomName
        } else {
            val bathroomNumberName = "Bathroom $bathroomNumber"
            viewBinding.formTitle.text = bathroomNumberName
        }

    }


    private fun saveBathroomName(position: Int) {
        val bathroomName = viewBinding.bathroomNameEditText.text
        FormsDummyData.numberOfBathroom[position - 1].bathroomName = bathroomName.toString()
        viewBinding.formTitle.text = FormsDummyData.numberOfBathroom[position - 1].bathroomName
        viewBinding.bathroomNameEditText.setText("")
        viewBinding.bathroomNameEditText.visibility = View.GONE
        viewBinding.previous.visibility = View.VISIBLE
        viewBinding.next.visibility = View.VISIBLE
        viewBinding.formTitle.visibility = View.VISIBLE
    }


    // set adapter as the saved data in database
    fun setDataOnAdapter() {
        try {
            if (FormsDummyData.numberOfBathroom.isNotEmpty()) {
                setFixDataToField(FormsDummyData.numberOfBathroom)
                getOkImage(FormsDummyData.numberOfBathroom)
                totalNumberOfBathroom = FormsDummyData.numberOfBathroom.size
                if (FormsDummyData.numberOfBathroom[0].bathroomName != getString(R.string.bathroom)) {
                    viewBinding.formTitle.text = FormsDummyData.numberOfBathroom[0].bathroomName
                } else {
                    val bathroomNumberName = "Bathroom $bathroomNumber"
                    viewBinding.formTitle.text = bathroomNumberName
                }

                bathChildAdapter = BathroomChildAdapter(
                    requireContext(), FormItemList.bathroomItemsList,
                    FormsDummyData.numberOfBathroom[0].list, 0

                )
                MainScope().launch {
                    viewBinding.recyclerView.setItemViewCacheSize(FormsDummyData.numberOfBathroom[0].list.size)
                    viewBinding.recyclerView.adapter = bathChildAdapter
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setDataToAdapterWithPosition(position: Int) {
        viewBinding.formTitle.text = FormsDummyData.numberOfBathroom[position].bathroomName
        bathChildAdapter = BathroomChildAdapter(
            requireContext(), FormItemList.bathroomItemsList,
            FormsDummyData.numberOfBathroom[position].list, position

        )
        MainScope().launch {
            viewBinding.recyclerView.setItemViewCacheSize(FormsDummyData.numberOfBathroom[position].list.size)
            viewBinding.recyclerView.adapter = bathChildAdapter
        }
    }


    //save  fix data in bathroomFix list

    fun setFixDataToField(bathroomData: List<Bathroom>) {
        Constants.bathroomFixList.clear()
        for (element in bathroomData) {
            val list = mutableListOf<BathroomFix>()
            for (j in 0 until element.list.size) {
                list.add(element.list[j].fix)
            }
            Constants.bathroomFixList.add(list)
        }
    }


    // set data in fix if already data saved in database fix item in bathroom list

    fun setDataToFix() {
        for (i in 0 until FormsDummyData.numberOfBathroom.size) {
            for (j in 0 until FormsDummyData.numberOfBathroom[i].list.size) {
                if (FormsDummyData.numberOfBathroom[i].list[j].fix.fix == getString(R.string.itemFIX)) {
                    FormsDummyData.numberOfBathroom[i].list[j].fix = Constants.bathroomFixList[i][j]
                }
            }
        }
    }

    // set data in ok  if already data saved in database ok item in bathroom list

    private fun setOkImage() {
        for (i in 0 until FormsDummyData.numberOfBathroom.size) {
            for (j in 0 until FormsDummyData.numberOfBathroom[i].list.size) {

                if (FormsDummyData.numberOfBathroom[i].list[j].ok.ok == getString(R.string.itemOK)) {
                    FormsDummyData.numberOfBathroom[i].list[j].ok =
                        Constants.bathroomOkImageList[i][j]
                }
            }
        }
    }

    // save ok item data in bathroomOkImage list
    private fun getOkImage(bathroomData: List<Bathroom>) {
        Constants.bathroomOkImageList.clear()
        for (element in bathroomData) {
            val list = mutableListOf<BathroomOk>()
            for (j in 0 until element.list.size) {
                list.add(element.list[j].ok)
            }
            Constants.bathroomOkImageList.add(list)
        }
    }

}