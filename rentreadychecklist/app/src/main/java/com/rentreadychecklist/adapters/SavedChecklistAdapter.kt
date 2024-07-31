package com.rentreadychecklist.adapters

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rentreadychecklist.R
import com.rentreadychecklist.RoomUpdateInterface
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.newAndSavedChecklist
import com.rentreadychecklist.db.AppData
import com.rentreadychecklist.pdf.PdfGenerator
import com.rentreadychecklist.utils.CheckPermission
import com.rentreadychecklist.utils.ProgressBarDialogPdf
import com.rentreadychecklist.viewmodel.AppViewModel
/**
 * Adapter for set SavedList to row and we have some features like send , delete and download PDF.
 */
class SavedChecklistAdapter(
    private val context: Context,
    private val savedCheckList: ArrayList<AppData>,
    lifeCycleStoreOwner: ViewModelStoreOwner,
    private val checkSavedChecklist: RoomUpdateInterface
) : RecyclerView.Adapter<SavedChecklistAdapter.ViewHolder>() {

    val list: MutableList<AppData> = savedCheckList
    val viewModel = ViewModelProvider(lifeCycleStoreOwner)[AppViewModel::class.java]
    val progressBarDialog = ProgressBarDialogPdf(context)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var savedCheckListName: TextView

        init {
            savedCheckListName = view.findViewById(R.id.savedChecklistName)

            view.setOnClickListener {
                FORMID = savedCheckList[bindingAdapterPosition].id.toLong()
                newAndSavedChecklist = context.resources.getString(R.string.saved)
                val navController = Navigation.findNavController(view)
                navController.navigate(R.id.navigationDrawerFragment)
            }

            view.setOnLongClickListener {

                val bottomDialog = BottomSheetDialog(view.context)
                bottomDialog.setContentView(R.layout.saved_bottom_sheet_dialog)

                val sendBtn = bottomDialog.findViewById<LinearLayout>(R.id.sendPdf)
                val downloadBtn = bottomDialog.findViewById<LinearLayout>(R.id.downloadPdf)
                val deleteBtn = bottomDialog.findViewById<LinearLayout>(R.id.deletePdf)
                val title = bottomDialog.findViewById<TextView>(R.id.title)

                title?.text =
                    if (list[bindingAdapterPosition].homeScreen[0].propertyAddress.isNotEmpty()) {
                        list[bindingAdapterPosition].homeScreen[0].propertyAddress
                    } else {
                        val id = list[bindingAdapterPosition].id
                        "Checklist $id"
                    }
                sendBtn?.setOnClickListener {
                    bottomDialog.dismiss()
                    FORMID = list[bindingAdapterPosition].id.toLong()
                    val navController = Navigation.findNavController(view)
                    navController.navigate(R.id.emailFragment2)
                }
                downloadBtn?.setOnClickListener {
                    bottomDialog.dismiss()
                    downloadPdf(bindingAdapterPosition)
                }
                deleteBtn?.setOnClickListener {
                    bottomDialog.dismiss()
                    deleteCheckList()
                }
                bottomDialog.show()

                true
            }
        }

        private fun downloadPdf(position: Int) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val checkPermission = CheckPermission(context)
                    checkPermission.showStoragePermissionDialog()

                } else {
                    generatePdf(position)
                }
            } else {
                generatePdf(position)
            }
        }

        private fun generatePdf(position: Int) {
            progressBarDialog.showProgressBar()

            try {
                PdfGenerator(context).apply {
                    val pdfList: ArrayList<AppData> = arrayListOf()
                    pdfList.add(list[position])
                    if (list[bindingAdapterPosition].homeScreen[0].propertyAddress.isNotEmpty()) {
                        val propertyAddressWithTime = list[bindingAdapterPosition]
                            .homeScreen[0]
                        val propertyAddressWithTimeString = propertyAddressWithTime
                            .propertyAddress.replace(("[^\\w\\d ]").toRegex(), "_") + ".pdf"
                        pdfFileInDownload(pdfList, progressBarDialog, propertyAddressWithTimeString)

                    } else {
                        val id = list[position].id
                        pdfFileInDownload(pdfList, progressBarDialog, "Checklist $id.pdf")
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun deleteCheckList() {

                val deleteDialog = Dialog(context)
                deleteDialog.setContentView(R.layout.delete_dialog)
                val yesButton = deleteDialog.findViewById<Button>(R.id.yes)
                val noButton = deleteDialog.findViewById<Button>(R.id.no)
                deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                deleteDialog.show()
                yesButton.setOnClickListener {
                    viewModel.deleteFormInfo(list[bindingAdapterPosition])
                    list.removeAt(bindingAdapterPosition)
                    notifyItemRemoved(bindingAdapterPosition)
                    if (list.isEmpty()) {
                        checkSavedChecklist.checkSavedList()
                    }
                    deleteDialog.dismiss()
                }
                noButton.setOnClickListener {
                    deleteDialog.dismiss()
                }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.saved_checklist_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (savedCheckList[position].homeScreen[0].propertyAddress == "") {
            val savedCheckListName = context.resources.getString(R.string.adapterChecklist) +
                    savedCheckList[position].id.toString()
            holder.savedCheckListName.text = savedCheckListName
        } else {
            holder.savedCheckListName.text = savedCheckList[position].homeScreen[0].propertyAddress
        }

    }

    override fun getItemCount(): Int {
        return savedCheckList.size
    }

}