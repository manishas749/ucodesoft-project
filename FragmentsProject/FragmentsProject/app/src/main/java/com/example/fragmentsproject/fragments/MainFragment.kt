package com.example.fragmentsproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentsproject.R
import com.example.fragmentsproject.adapter.CustomAdapter
import com.example.fragmentsproject.databinding.FragmentMainBinding
import com.example.fragmentsproject.utils.SharedPreference


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private lateinit var adapter: CustomAdapter
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        binding.logout.setOnClickListener {
            sharedPreference.clearToken()
            findNavController().navigate(R.id.action_mainFragment_to_registerationFragment)

        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var list: ArrayList<String> = arrayListOf()
        list.add("Manisha")
        list.add("ABC")
        list.add("ABC")

        list.add("ABC")

        list.add("ABC")

        list.add("ABC")

        list.add("ABC")
        list.add("ABC")



        adapter = CustomAdapter(list, requireContext())
        binding.recyclerView.adapter = adapter


    }


}