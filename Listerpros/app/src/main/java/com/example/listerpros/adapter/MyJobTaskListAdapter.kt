package com.example.listerpros.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.listerpros.R
import com.example.listerpros.`interface`.MyJobTaskListClickListener
import com.example.listerpros.constants.Constants.Companion.JOB_TASK_STATUS_COUNT
import com.example.listerpros.fragments.SkipNoteFragment
import com.example.listerpros.model.getjobdetails.Task
import com.example.listerpros.model.updatetaskstatus.UpdateTaskStatus
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.viewmodel.UpdateTaskStatusViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Adapter for recycleView of my job tasks

class MyJobTaskListAdapter(
    val context: Context, val list: List<Task>,
    private val myJobTaskListClickListener: MyJobTaskListClickListener,
    lifeCycleStoreOwner: ViewModelStoreOwner,
    private val lifeCycleOwner: LifecycleOwner
) : RecyclerView.Adapter<MyJobTaskListAdapter.ViewHolder>() {
    val progressBarDialog = ProgressBarDialog(context)
    val viewModel = ViewModelProvider(lifeCycleStoreOwner)[UpdateTaskStatusViewModel::class.java]
    var taskDoneBoolean = false

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView
        val skipTextView: TextView
        val doneButton: Button
        val doneCheckMark: ImageView
        private val reasonForSkipTextView: TextView
        val reasonForSkipImageView: ImageView
        val reasonForSkipSubmitTextView: TextView
        val constraintLayout: ConstraintLayout
        val taskSkipped: TextView


        init {
            taskName = itemView.findViewById(R.id.myJobTask)
            skipTextView = itemView.findViewById(R.id.skipTaskTextView)
            doneButton = itemView.findViewById(R.id.doneTask)
            doneCheckMark = itemView.findViewById(R.id.doneCheckmark)
            reasonForSkipTextView = itemView.findViewById(R.id.reasonForSkipTextView)
            reasonForSkipImageView = itemView.findViewById(R.id.reasonForSkipImg)
            reasonForSkipSubmitTextView = itemView.findViewById(R.id.reasonForSkipSubmitTextView)
            constraintLayout = itemView.findViewById(R.id.reasonForSkipConstraint1)
            taskSkipped = itemView.findViewById(R.id.professionalSkipped)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.job_task_row, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskListItem = list[position]
        holder.taskName.text = taskListItem.name

        val skipNote = taskListItem.notes
        holder.reasonForSkipSubmitTextView.text = skipNote.notes

        holder.skipTextView.setOnClickListener {
            holder.constraintLayout.visibility = View.VISIBLE
        }

        if (taskListItem.status == "COMPLETED") {
            holder.doneButton.visibility = View.GONE
            holder.doneCheckMark.visibility = View.VISIBLE
            holder.constraintLayout.visibility = View.GONE
            holder.taskSkipped.visibility = View.GONE
            holder.taskName.isEnabled = false
            holder.skipTextView.visibility = View.GONE
            JOB_TASK_STATUS_COUNT++
            myJobTaskListClickListener.onCellClickListener()
        }

        if (taskListItem.status == "SKIPPED") {
            holder.doneButton.visibility = View.GONE
            holder.reasonForSkipImageView.visibility = View.GONE
            holder.constraintLayout.visibility = View.GONE
            holder.taskSkipped.visibility = View.VISIBLE
            holder.skipTextView.visibility = View.GONE
            JOB_TASK_STATUS_COUNT++
            myJobTaskListClickListener.onCellClickListener()
        }

        holder.reasonForSkipImageView.setOnClickListener {
            val taskId = taskListItem.id
            val taskName = taskListItem.name
            val bundle = Bundle()
            bundle.putInt("taskId", taskId)
            bundle.putString("taskName", taskName)
            val skipNotes = SkipNoteFragment()
            skipNotes.arguments = bundle
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.skipNoteFragment, args = bundle)

        }

        holder.taskName.setOnClickListener {
            if (holder.constraintLayout.visibility == View.GONE) {
                holder.constraintLayout.visibility = View.VISIBLE
            } else {
                holder.constraintLayout.visibility = View.GONE
            }
        }

        holder.doneButton.setOnClickListener {

          progressBarDialog.showProgressBar()
            viewModel.updateTaskStatus(
                taskListItem.id.toString(),
                "",
                holder.itemView.resources.getString(R.string.job_task_completed)
            )
            if (!viewModel.responseUpdateTaskStatus.hasObservers()) {
                updateTaskStatus(holder)
                Log.d("observer", "observer")

            }

            if (taskDoneBoolean){
                Log.d("observer", "true")
                holder.doneCheckMark.visibility = View.VISIBLE
                holder.doneButton.visibility = View.GONE
                holder.constraintLayout.visibility = View.GONE
                holder.taskName.isEnabled = false
                holder.skipTextView.visibility = View.GONE
                taskDoneBoolean = false
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun notityTaskDoneOrSkip(position: Int){
        notifyItemChanged(position)
    }


    private fun updateTaskStatus(holder: ViewHolder) {

        viewModel.responseUpdateTaskStatus.observe(lifeCycleOwner, Observer { call ->
            call.clone().enqueue(object : Callback<UpdateTaskStatus> {
                override fun onResponse(
                    call: Call<UpdateTaskStatus>,
                    response: Response<UpdateTaskStatus>
                ) {
                    if (response.isSuccessful) {
                        progressBarDialog.hideProgressBar()
                        Toast.makeText(
                            holder.itemView.context,
                            holder.itemView.resources.getString(R.string.done),
                            Toast.LENGTH_SHORT
                        ).show()

                        JOB_TASK_STATUS_COUNT++
                       // taskDoneBoolean = true
                        notityTaskDoneOrSkip(holder.adapterPosition)
                        myJobTaskListClickListener.onCellClickListener()
                        Log.d("interface", "interface")
                        holder.doneCheckMark.visibility = View.VISIBLE
                        holder.doneButton.visibility = View.GONE
                        holder.constraintLayout.visibility = View.GONE
                        holder.taskName.isEnabled = false
                        holder.skipTextView.visibility = View.GONE


                    } else {
                        progressBarDialog.hideProgressBar()
                        Toast.makeText(
                            holder.itemView.context,
                            holder.itemView.resources.getString(R.string.please_select_on_location),
                            Toast.LENGTH_SHORT
                        ).show()
                        taskDoneBoolean = false
                    }
                }

                override fun onFailure(call: Call<UpdateTaskStatus>, t: Throwable) {
                    progressBarDialog.hideProgressBar()
                    Toast.makeText(
                        holder.itemView.context,
                        holder.itemView.resources.getString(R.string.tryAgain),
                        Toast.LENGTH_SHORT
                    ).show()
                    taskDoneBoolean = false
                }
            })
        })
    }
}