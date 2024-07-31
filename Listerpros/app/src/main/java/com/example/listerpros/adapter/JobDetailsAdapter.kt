package com.example.listerpros.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.listerpros.R
import com.example.listerpros.constants.Constants.Companion.JOB_ID
import com.example.listerpros.fragments.JobDetailsFragment
import com.example.listerpros.model.getmyjobs.Data


//Adapter for job detail fragment
class JobDetailsAdapter(list: List<Data>):RecyclerView.Adapter<JobDetailsAdapter.ViewHolder>() {

    private var jobDetailList = list

    open inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var jobTitle: TextView
        var location: TextView
        var progressStatus: TextView
        var jobStartTime:  TextView
        var jobEndTime:  TextView
        init {
            jobTitle = view.findViewById(R.id.title)
            location = view.findViewById(R.id.locationTextView)
            progressStatus = view.findViewById(R.id.progressStatus)
            jobStartTime = view.findViewById(R.id.job_start_time)
            jobEndTime = view.findViewById(R.id.job_end_time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_jobs_job_details_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val jobDetailPosition = jobDetailList[position]
        holder.jobTitle.text = jobDetailPosition.title
        holder.location.text = jobDetailPosition.location
        holder.progressStatus.text = jobDetailPosition.status
        holder.jobStartTime.text = jobDetailPosition.start_date.subSequence(11,16)
        holder.jobEndTime.text = jobDetailPosition.end_date.subSequence(11,16)
        //to change color of progress button//

        if (holder.progressStatus.text.toString() == holder.itemView.context.resources.getString(R.string.inProgress)){
            progressStatusColor(holder, holder.itemView.context.resources.getString(R.string.yellow))
        }else if (holder.progressStatus.text.toString() == holder.itemView.context.resources.getString(R.string.newColor)){
            progressStatusColor(holder, holder.itemView.context.resources.getString(R.string.greenColor))
        }else
        {
            progressStatusColor(holder, holder.itemView.context.resources.getString(R.string.blueColor))
        }

        holder.itemView.setOnClickListener{

            JOB_ID = jobDetailPosition.id
            val bundle = Bundle()
            bundle.putInt("jobId", jobDetailPosition.id)
            val jobDetailFragment = JobDetailsFragment()
            jobDetailFragment.arguments = bundle
            findNavController(holder.itemView).navigate(R.id.jobDetailsFragment, args = bundle)
        }
    }

    // progress Status color change
    private fun progressStatusColor(holder:ViewHolder, color:String){

        when (color){
            "Yellow" -> holder.progressStatus.setTextColor(Color.parseColor("#FFB800"))
            "Green" ->  holder.progressStatus.setTextColor(Color.parseColor("#10BA3F"))
            else -> holder.progressStatus.setTextColor(Color.parseColor("#1397FF"))
        }

    }
}