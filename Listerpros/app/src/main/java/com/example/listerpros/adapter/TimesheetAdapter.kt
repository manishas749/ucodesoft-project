package com.example.listerpros.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.listerpros.R
import com.example.listerpros.model.timesheet.gettimesheet.TimeSheetData
import com.example.listerpros.utils.Helper
import com.example.listerpros.utils.NetworkCheck
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

//Adapter for timeSheet fragment
class TimesheetAdapter(val context: Context, private val timeSheetList: List<TimeSheetData>) :
    RecyclerView.Adapter<TimesheetAdapter.UserViewHolder>() {


    //View Holder class
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime: TextView
        var endTime: TextView
        var timeDifference: TextView
        var networkCheck: NetworkCheck

        init {
            startTime = itemView.findViewById(R.id.startTime)
            endTime = itemView.findViewById(R.id.endTime)
            timeDifference = itemView.findViewById(R.id.text)
            networkCheck = NetworkCheck(context)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.time_sheet_recycle_view_list, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.startTime.text = setStartEndTime(position).first
        holder.endTime.text = setStartEndTime(position).second
        val list = timeSheetList[position]

        try {
            holder.timeDifference.text =
                Helper.timeDiffernce(list.check_in_time.subSequence(11, 19).toString(),
                    list.check_out_time.subSequence(11, 19).toString()).first
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }

        if (holder.endTime.text.isEmpty()) {
            holder.timeDifference.text = holder.itemView.resources.getString(R.string.inProgress)
        }
        if (holder.timeDifference.text == holder.itemView.resources.getString(R.string.inProgress)) {
            holder.timeDifference.setTextColor(Color.parseColor("#FFB800"))
        } else {
            holder.timeDifference.setTextColor(Color.parseColor("#FF000000"))
        }
        holder.itemView.setOnClickListener()
        {
            navigateToMapFragment(holder.itemView, holder)
        }
    }

    private fun setStartEndTime(position: Int): Pair<String, String> {
        val list = timeSheetList[position]
        val startDateFormat: Date?
        val endDateFormat: Date?
        var startTime = ""
        var endTime = ""
        try {
            val startTimeList = list.check_in_time.subSequence(11, 19)
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.US)
            startDateFormat = formatter.parse(startTimeList.toString())
            startTime = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(startDateFormat!!)
            val endTimeList = list.check_out_time.subSequence(11, 19)
            endDateFormat = formatter.parse(endTimeList.toString())
            endTime = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(endDateFormat!!)
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }

        return Pair(startTime, endTime)
    }


    private fun navigateToMapFragment(itemView: View, holder: UserViewHolder) {
        val networkCheck = holder.networkCheck.checkNetwork()
        if (networkCheck) {
            findNavController(itemView).navigate(R.id.timeSheetMapFragment)
        } else {
            Toast.makeText(context,
                itemView.resources.getString(R.string.noInternet),
                Toast.LENGTH_SHORT).show()
        }
    }


    override fun getItemCount(): Int {
        return timeSheetList.size

    }


}



