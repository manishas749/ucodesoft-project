package com.example.listerpros.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.listerpros.R
import com.example.listerpros.constants.Constants.Companion.NOTE_ID
import com.example.listerpros.fragments.AddNotesFragment
import com.example.listerpros.model.getjobdetails.Note


//Adapter to add notes in ADD notes fragment

class AddNotesAdapter(private val list: List<Note>, val title: String, private val jobId:String) :
    RecyclerView.Adapter<AddNotesAdapter.ViewHolder>() {

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var addNote: TextView

        init {
            addNote = view.findViewById(R.id.AddNotes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.job_detail_notes_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addNotePosition = list[position]
        holder.addNote.text = addNotePosition.notes
        holder.itemView.setOnClickListener()
        {
            NOTE_ID = addNotePosition.id
            val notes= addNotePosition.notes
            val jobId=jobId
            val bundle = Bundle()
            bundle.putInt("noteId", NOTE_ID)
            bundle.putString("notes",notes)
            bundle.putString("title",title)
            bundle.putString("noteJobId",jobId)
            val addNotesFragment = AddNotesFragment()
            addNotesFragment.arguments = bundle
            Navigation.findNavController(holder.itemView).navigate(R.id.addNotesFragment, args = bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

