package com.example.listerpros.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.listerpros.R
import com.example.listerpros.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController:NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        bottomNavigation()
        colorNavigationBottomIcon()

        binding.bottomNavView.setupWithNavController(navController = navController)

    }

    private fun bottomNavigation(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.myJobsFragment -> showBottomNavigation()
                R.id.timeSheetsFragment -> showBottomNavigation()
                R.id.myAccountFragment ->showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }

    }

    private fun hideBottomNavigation(): Boolean {
        binding.bottomNavView.visibility = View.GONE
        return true
    }
    private fun showBottomNavigation(): Boolean {
        binding.bottomNavView.visibility = View.VISIBLE
        return true
    }

    private fun colorNavigationBottomIcon()
    {
        val iconColorStates = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)), intArrayOf(
            Color.parseColor("#77869E"),
            Color.parseColor("#007BFF")
        ))

        binding.bottomNavView.itemIconTintList=iconColorStates
        binding.bottomNavView.itemTextColor=iconColorStates
    }

}