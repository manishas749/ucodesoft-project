package com.example.listerpros.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listerpros.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    private val context: Context,
    private val data: ArrayList<Date>,
    currentDate: Calendar,
    changeMonth: Calendar?
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var mListener: OnItemClickListener? = null
    private var index = -1

    // This is true only the first time you load the calendar.
    private var selectCurrentDate = true
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val selectedDay =
        when {
            changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            else -> currentDay
        }
    private val selectedMonth =
        when {
            changeMonth != null -> changeMonth[Calendar.MONTH]
            else -> currentMonth
        }
    private val selectedYear =
        when {
            changeMonth != null -> changeMonth[Calendar.YEAR]
            else -> currentYear
        }

    inner class ViewHolder(itemView: View, val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var txtDay: TextView
        var txtDayInWeek: TextView
        var cardView: CardView

        init {
            txtDay = itemView.findViewById(R.id.txt_date)
            txtDayInWeek = itemView.findViewById(R.id.txt_day)
            cardView = itemView.findViewById(R.id.cardView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_job_calender_row, parent, false)
        return ViewHolder(view, mListener!!)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.time = data[holder.adapterPosition]

        //Set the year, month and day that is gonna be displayed
        val displayMonth = cal[Calendar.MONTH]
        val displayYear = cal[Calendar.YEAR]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        //Set text to txtDayInWeek and txtDay.
        try {
            val dayInWeek = data[position]
            sdf.applyPattern("EEE")
            val firstLetterDay = sdf.format(dayInWeek).toString()
            holder.txtDayInWeek.text = firstLetterDay[0].toString()

        } catch (ex: ParseException) {
            Log.v("Exception", ex.localizedMessage!!)
        }
        holder.txtDay.text = cal[Calendar.DAY_OF_MONTH].toString()

        holder.cardView.setOnClickListener {
            index = holder.adapterPosition
            selectCurrentDate = false
            holder.listener.onItemClick(holder.adapterPosition)
            notifyDataSetChanged()
        }

        if (index == holder.adapterPosition) {
           makeItemSelected(holder)
        } else {
            if (displayDay == selectedDay
                && displayMonth == selectedMonth
                && displayYear == selectedYear
                && selectCurrentDate
            ) {
                makeItemSelected(holder)
            } else {
                makeItemDefault(holder)
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    //This make the item selected.
    private fun makeItemSelected(holder: ViewHolder) {
        holder.txtDay.setTextColor(Color.parseColor("#FFFFFF"))
        holder.txtDayInWeek.setTextColor(Color.parseColor("#FFFFFF"))
        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.button_color
            )
        )
        holder.cardView.isEnabled = false
    }

    //This make the item default.
    private fun makeItemDefault(holder: ViewHolder) {
        holder.txtDay.setTextColor(Color.parseColor("#042C5C"))
        holder.txtDayInWeek.setTextColor(Color.parseColor("#042C5C"))
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        holder.cardView.isEnabled = true
    }
}
