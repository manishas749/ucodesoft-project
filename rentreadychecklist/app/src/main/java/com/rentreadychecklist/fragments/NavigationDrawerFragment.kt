package com.rentreadychecklist.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.rentreadychecklist.R
import com.rentreadychecklist.databinding.FragmentNavigationDrawerBinding
import com.rentreadychecklist.utils.Helper.Companion.hideKeyBoard

/**
 * This class is used for navigate to any Checklist FORM.
 */
class NavigationDrawerFragment : Fragment() {

    private lateinit var viewBinding: FragmentNavigationDrawerBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var clickNavItem: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentNavigationDrawerBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.setSupportActionBar(viewBinding.toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.show()

        navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val toggle = ActionBarDrawerToggle(
            activity,
            viewBinding.drawerLayout,
            viewBinding.toolbar,
            R.string.app_name,
            R.string.app_name
        )

        toggle.drawerArrowDrawable.color = Color.WHITE
        viewBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        viewBinding.navigationView.itemIconTintList = null


        // navigation drawer destination set
        viewBinding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.outsideFormFragment -> {
                    clickNavItem = R.id.outsideFormFragment
                }
                R.id.frontDoorsFormFragment2 -> {
                    clickNavItem = R.id.frontDoorsFormFragment2
                }
                R.id.garageFormFragment -> {
                    clickNavItem = R.id.garageFormFragment
                }
                R.id.laundryRoomFormFragment -> {
                    clickNavItem = R.id.laundryRoomFormFragment
                }
                R.id.livingRoomFormFragment -> {
                    clickNavItem = R.id.livingRoomFormFragment
                }
                R.id.greatRoomFormFragment -> {
                    clickNavItem = R.id.greatRoomFormFragment
                }
                R.id.diningRoomFormFragment -> {
                    clickNavItem = R.id.diningRoomFormFragment
                }
                R.id.kitchenFormFragment -> {
                    clickNavItem = R.id.kitchenFormFragment
                }
                R.id.miscellaneousFromFragment -> {
                    clickNavItem = R.id.miscellaneousFromFragment
                }
                R.id.numberOfBedroomsFragment -> {
                    clickNavItem = R.id.numberOfBedroomsFragment
                }
                R.id.numberOfBathroomsFragment -> {
                    clickNavItem = R.id.numberOfBathroomsFragment
                }
                else -> {
                    //todo
                }

            }
            viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        // navigation drawer destination set
        viewBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // hideKeyBoard(view)
            }

            override fun onDrawerOpened(drawerView: View) {
                hideKeyBoard(view)
            }

            override fun onDrawerClosed(drawerView: View) {
                hideKeyBoard(view)
                if (clickNavItem != 0) {
                    if (navController.currentDestination?.id != R.id.newChecklistFragment2) {
                        navController.popBackStack()
                    }
                }


                when (clickNavItem) {
                    R.id.outsideFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.outsideFormFragment)
                    }
                    R.id.frontDoorsFormFragment2 -> {
                        clickNavItem = 0
                        navController.navigate(R.id.frontDoorsFormFragment2)
                    }
                    R.id.garageFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.garageFormFragment)
                    }
                    R.id.laundryRoomFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.laundryRoomFormFragment)
                    }
                    R.id.livingRoomFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.livingRoomFormFragment)
                    }
                    R.id.greatRoomFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.greatRoomFormFragment)
                    }
                    R.id.diningRoomFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.diningRoomFormFragment)
                    }
                    R.id.kitchenFormFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.kitchenFormFragment)
                    }
                    R.id.miscellaneousFromFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.miscellaneousFromFragment)
                    }
                    R.id.numberOfBedroomsFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.numberOfBedroomsFragment)
                    }
                    R.id.numberOfBathroomsFragment -> {
                        clickNavItem = 0
                        navController.navigate(R.id.numberOfBathroomsFragment)
                    }
                    R.id.additionalNotesFragment -> {
                        clickNavItem = 0
                    }
                    else -> {
                        //todo
                    }
                }
            }

            override fun onDrawerStateChanged(newState: Int) {
                //todo
            }

        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        exitDialog()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    // to reconfirm dialog if wants to exit the checklist
    fun exitDialog() {
        val exitDialog = Dialog(requireContext())
        exitDialog.setContentView(R.layout.exit_dialog)
        val yesButton = exitDialog.findViewById<Button>(R.id.yes)
        val noButton = exitDialog.findViewById<Button>(R.id.no)
        exitDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        exitDialog.show()
        yesButton.setOnClickListener {
            exitDialog.dismiss()
            Navigation.findNavController(viewBinding.root).popBackStack()
        }
        noButton.setOnClickListener {
            exitDialog.dismiss()
        }
    }

}