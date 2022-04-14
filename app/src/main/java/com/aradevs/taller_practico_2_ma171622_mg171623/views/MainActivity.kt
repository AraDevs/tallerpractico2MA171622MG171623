package com.aradevs.taller_practico_2_ma171622_mg171623.views

import android.app.ActivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aradevs.taller_practico_2_ma171622_mg171623.R
import com.aradevs.taller_practico_2_ma171622_mg171623.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainActivityViewModel by viewModels()
    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this::onDestinationChanged)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        args: Bundle?,
    ) {
        binding.bottomNav.isVisible =
            destination.id != R.id.medicine_detail_fragment && destination.id != R.id.shopping_cart_fragment
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.medicine_list_fragment -> finish()
            else -> {
                //do nothing
            }
        }
        super.onBackPressed()
    }
}