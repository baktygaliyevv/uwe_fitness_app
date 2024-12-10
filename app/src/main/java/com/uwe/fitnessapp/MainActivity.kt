package com.uwe.fitnessapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.uwe.fitnessapp.databinding.ActivityMainBinding
import com.uwe.fitnessapp.utils.LogUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_plans, R.id.navigation_log, R.id.navigation_exercises, R.id.navigation_tools
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                // top level fragments
                R.id.navigation_exercises,
                R.id.navigation_plans,
                R.id.navigation_log,
                R.id.navigation_tools -> {
                    navView.menu.findItem(destination.id).isChecked = true
                }
                // nested fragments for tools
                R.id.navigation_bmi,
                R.id.navigation_calories,
                R.id.navigation_heart_rate -> {
                    navView.menu.findItem(R.id.navigation_tools).isChecked = true
                }
                // nested fragments for logs
                R.id.navigation_exercise_log,
                R.id.navigation_exercise_sets -> {
                    navView.menu.findItem(R.id.navigation_log).isChecked = true
                }
                // nested fragments for plans
                R.id.navigation_plan_detail,
                R.id.navigation_transition -> {
                    navView.menu.findItem(R.id.navigation_plans).isChecked = true
                }
                // nested fragments for exercises
                R.id.navigation_exercises_list,
                R.id.navigation_transition -> {
                    navView.menu.findItem(R.id.navigation_exercises).isChecked = true
                }
                // exercises_exercise
                R.id.navigation_exercises_exercise -> {
                    val source = arguments?.getString("source")
                    when (source) {
                        "exercises" -> navView.menu.findItem(R.id.navigation_exercises).isChecked = true
                        "plans" -> navView.menu.findItem(R.id.navigation_plans).isChecked = true
                    }
                }
            }
        }

        LogUtils.printLogsJson(this)

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}