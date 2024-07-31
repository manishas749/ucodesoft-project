package com.example.notesappusingviewmodel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.datamodel.Notes
import com.example.notesappusingviewmodel.R

var notesList = emptyList<Notes>()

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {
    private lateinit var mlistener: AdapterView.OnItemClickListener

    interface onItemClickListener
        : AdapterView.OnItemClickListener {
        fun onItemClick(position: Int)
        }

    fun setOnClickListener(listener: onItemClickListener) {
        mlistener = listener
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var description: TextView


        init {
            title = itemView.findViewById(R.id.Textview1)
            description = itemView.findViewById(R.id.Textview2)
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false),
            mlistener as onItemClickListener
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = notesList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description


    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(notes: List<Notes>) {
        notesList = notes
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): Notes {
        return notesList.get(position)

    }


}
