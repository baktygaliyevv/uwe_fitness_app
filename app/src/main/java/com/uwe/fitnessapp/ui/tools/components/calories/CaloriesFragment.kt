package com.uwe.fitnessapp.ui.tools.components.calories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwe.fitnessapp.databinding.FragmentCaloriesBinding
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.models.CalorieGoal
import kotlin.math.roundToInt

class CaloriesFragment : Fragment() {

    // view binding for fragment_calories layout
    private var _binding: FragmentCaloriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaloriesBinding.inflate(inflater, container, false)

        // define activity levels and corresponding activity factors for calculations
        val activityLevels = listOf(
            "Basal Metabolic Rate",
            "Sedentary (little or no exercise)",
            "Light (exercise 1-3 times a week)",
            "Moderate (exercise 4-5 times a week)",
            "Active (daily exercise or intense exercise 3-4 times a week)",
            "Very Active (daily intense exercise)"
        )

        val activityFactors = mapOf(
            "Basal Metabolic Rate" to 1.0,
            "Sedentary (little or no exercise)" to 1.2,
            "Light (exercise 1-3 times a week)" to 1.3,
            "Moderate (exercise 4-5 times a week)" to 1.5,
            "Active (daily exercise or intense exercise 3-4 times a week)" to 1.7,
            "Very Active (daily intense exercise)" to 1.9
        )

        // set up dropdown adapter for activity levels
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, activityLevels)
        binding.activityDropdown.setAdapter(adapter)

        // set click listener for the calculate button
        binding.calculateButton.setOnClickListener {
            calculateCalories(activityFactors)
        }

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return binding.root
    }

    // method to calculate daily calorie needs based on user input
    private fun calculateCalories(activityFactors: Map<String, Double>) {
        // reset any previous error messages
        binding.ageInputLayout.error = null
        binding.heightInputLayout.error = null
        binding.weightInputLayout.error = null
        binding.activityInputLayout.error = null
        binding.genderErrorTextView.visibility = View.GONE

        // retrieve inputs from the user
        val ageText = binding.ageInput.text.toString()
        val heightText = binding.heightInput.text.toString()
        val weightText = binding.weightInput.text.toString()
        val activityLevel = binding.activityDropdown.text.toString()

        // validate age input
        if (ageText.isEmpty()) {
            binding.ageInputLayout.error = "Please enter age"
            return
        }
        if (heightText.isEmpty()) {
            binding.heightInputLayout.error = "Please enter height"
            return
        }
        if (weightText.isEmpty()) {
            binding.weightInputLayout.error = "Please enter weight"
            return
        }
        if (activityLevel.isEmpty()) {
            binding.activityInputLayout.error = "Please select activity level"
            return
        }

        // validate gender selection
        val isMaleSelected = binding.radioButtonMale.isChecked
        val isFemaleSelected = binding.radioButtonFemale.isChecked
        if (!isMaleSelected && !isFemaleSelected) {
            binding.genderErrorTextView.visibility = View.VISIBLE
            return
        } else {
            binding.genderErrorTextView.visibility = View.GONE
        }

        // convert inputs to appropriate types and validate ranges
        val age = ageText.toIntOrNull()
        val height = heightText.toFloatOrNull()
        val weight = weightText.toFloatOrNull()

        if (age == null || age !in 13..80) {
            binding.ageInputLayout.error = "Age should be between 13 and 80"
            return
        }
        if (height == null || height !in 110f..240f) {
            binding.heightInputLayout.error = "Height should be between 110 and 240 cm"
            return
        }
        if (weight == null || weight !in 25f..300f) {
            binding.weightInputLayout.error = "Weight should be between 25 and 300 kg"
            return
        }

        // retrieve activity factor based on selected activity level
        val activityFactor = activityFactors[activityLevel] ?: 1.0

        // calculate base calories using the Harris-Benedict formula
        val baseCalories = if (isMaleSelected) {
            66 + (13.7 * weight) + (5 * height) - (6.8 * age)
        } else {
            655 + (9.6 * weight) + (1.8 * height) - (4.7 * age)
        }

        // adjust calories based on activity level
        val totalCalories = baseCalories * activityFactor

        // create a list of calorie goals for different weight change scenarios
        val calorieGoals = listOf(
            CalorieGoal("Maintain weight", totalCalories.roundToInt()),
            CalorieGoal("Mild weight loss\n0.25 kg/week", (totalCalories * 0.9).roundToInt()),
            CalorieGoal("Weight loss\n0.5 kg/week", (totalCalories * 0.8).roundToInt()),
            CalorieGoal("Extreme weight loss\n1 kg/week", (totalCalories * 0.7).roundToInt()),
            CalorieGoal("Mild weight gain\n0.25 kg/week", (totalCalories * 1.1).roundToInt()),
            CalorieGoal("Weight gain\n0.5 kg/week", (totalCalories * 1.19).roundToInt()),
            CalorieGoal("Fast weight gain\n1 kg/week", (totalCalories * 1.39).roundToInt())
        )

        // set up RecyclerView to display the calorie goals
        binding.calorieGoalsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            visibility = View.VISIBLE
            adapter = CaloriesGoalAdapter(calorieGoals)
        }
    }

    // clean up the view binding to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
