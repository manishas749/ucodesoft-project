package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.FragmentMainBinding
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
            val wic = WindowInsetsControllerCompat(this, decorView)
            wic.isAppearanceLightStatusBars = true
            navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bg_color)
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment



            navController = navHostFragment.navController

            setupWithNavController(bottomNavV, navController)

           navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.complaintsFragment ->
                    {
                        complaintToolbar.visibility = View.GONE
                        binding.bottomNavV.visibility = View.GONE
                        binding.addComplaintBtn.visibility = View.GONE
                    }
                    R.id.allComplaintsFragment -> {
                        complaintToolbar.visibility = View.VISIBLE
                        binding.bottomNavV.visibility = View.VISIBLE
                        binding.addComplaintBtn.visibility = View.VISIBLE
                    }
                    R.id.profileFragment ->
                    {
                        complaintToolbar.visibility = View.VISIBLE
                        binding.bottomNavV.visibility = View.VISIBLE
                        binding.addComplaintBtn.visibility = View.VISIBLE
                    }
                    R.id.complaintsDetailFragment -> {
                        complaintToolbar.visibility = View.GONE
                        binding.bottomNavV.visibility = View.GONE
                        binding.addComplaintBtn.visibility = View.GONE
                    }
                    R.id.myCitiesFragment ->{
                        complaintToolbar.visibility = View.GONE
                        binding.bottomNavV.visibility = View.GONE
                        binding.addComplaintBtn.visibility = View.GONE
                    }

                }
            }
            binding.bottomNavV.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.allComplaintsFragment ->{
                        navHostFragment.navController.popBackStack()
                        navController.navigate(R.id.allComplaintsFragment)
                        true
                    }
                    R.id.profileFragment ->{
                        navHostFragment.navController.popBackStack()
                        navController.navigate(R.id.profileFragment)
                        true
                    }

                    else -> {true}
                }
            }

           complaintToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.notification -> {
                       findNavController().navigate(R.id.notificationFragment)

                    }
                }
                false
            }

            binding.bottomNavV.itemIconTintList = null;

            addComplaintBtn.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_newComplaintFragment)
            }
        }
    }


}