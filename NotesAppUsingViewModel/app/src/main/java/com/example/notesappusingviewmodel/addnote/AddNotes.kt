package com.example.notesappusingviewmodel.addnote

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.datamodel.Notes
import com.example.notes_app.viewmodel.NotesViewModel
import com.example.notesappusingviewmodel.R
import com.example.notesappusingviewmodel.databinding.FragmentAddNotesBinding


class AddNotes : Fragment() {
    private lateinit var notesViewModel: NotesViewModel  //Initialize viewModel
    private lateinit var binding: FragmentAddNotesBinding
   private lateinit var arrayAdapter: ArrayAdapter<String>
   private var stringAutoShow: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentAddNotesBinding.inflate(inflater, container, false)

        //where user will get the autosuggestion in textBox last 5 title will displayed
          notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
          notesViewModel.readAllNotes.observe(viewLifecycleOwner, Observer { notes ->
          val titleNote: ArrayList<Notes> = notes as ArrayList<Notes>
             for(e in titleNote)
             {
                 stringAutoShow.add(e.title)
             }
           val autosuggestion=stringAutoShow.distinct()
             arrayAdapter= ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,autosuggestion.takeLast(5))
             binding.TitleTextBox.setAdapter(arrayAdapter)
         })

      // where user can press save button to save and data will save in database
        binding.saveButton.setOnClickListener{
            insertDataToDataBase()

        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun insertDataToDataBase() {
        val description = binding.DescriptionTextBox.text.toString()
        val title = binding.TitleTextBox.text.toString()
        if (inputCheck(title, description)) {
            val notes = Notes(0, title, description)    //Added in DataBase
            notesViewModel.addNotes(notes)
            Toast.makeText(requireContext(), "Note Added Successfully", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
            onDestroy() }
        else {

            Toast.makeText(requireContext(),  "Atleast title is Required", Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }

}
