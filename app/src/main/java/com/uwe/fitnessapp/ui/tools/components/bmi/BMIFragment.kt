package com.uwe.fitnessapp.ui.tools.components.bmi

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Section
import com.uwe.fitnessapp.databinding.FragmentBmiBinding
import kotlin.math.pow

class BMIFragment : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!
    private lateinit var bmiGauge: SpeedView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)

        bmiGauge = binding.bmiGauge
        bmiGauge.maxSpeed = 40f
        bmiGauge.withTremble = false
        bmiGauge.setStartDegree(180)
        bmiGauge.setEndDegree(360)
        bmiGauge.clearSections()
        val sectionWidth = 110f
        bmiGauge.addSections(
            Section(0f, 0.04f, Color.parseColor("#FF5722"), sectionWidth),    // Severe Thinness
            Section(0.04f, 0.08f, Color.parseColor("#FF9800"), sectionWidth),  // Moderate Thinness
            Section(0.08f, 0.14f, Color.parseColor("#FFEB3B"), sectionWidth),  // Mild Thinness
            Section(0.14f, 0.40f, Color.parseColor("#4CAF50"), sectionWidth),  // Normal
            Section(0.40f, 0.60f, Color.parseColor("#FFEB3B"), sectionWidth),  // Overweight
            Section(0.60f, 0.80f, Color.parseColor("#FF9800"), sectionWidth),  // Obese Class I
            Section(0.80f, 1f, Color.parseColor("#F44336"), sectionWidth)      // Obese Class II
        )


        binding.calculateButton.setOnClickListener {
            calculateBMI()
        }
        return binding.root
    }

    private fun calculateBMI() {
        val ageText = binding.ageInput.text.toString()
        val heightText = binding.heightInput.text.toString()
        val weightText = binding.weightInput.text.toString()

        binding.ageInputLayout.error = null
        binding.heightInputLayout.error = null
        binding.weightInputLayout.error = null

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
        try {
            val age = ageText.toInt()
            val weight = weightText.toFloat()
            val height = heightText.toFloat() / 100

            if (age !in 20..99) {
                binding.ageInputLayout.error = "Age should be between 20 and 99"
                return
            }

            if (height !in 1.15f..2.40f) {
                binding.heightInputLayout.error = "Height should be between 115 and 240 cm"
                return
            }

            if (weight !in 25f..300f) {
                binding.weightInputLayout.error = "Weight should be between 25 and 300 kg"
                return
            }

            val bmi = weight / (height.pow(2))
            displayBMIResult(bmi, height)

        } catch (e: NumberFormatException) {
            binding.ageInputLayout.error = "Enter a valid number for age"
            binding.heightInputLayout.error = "Enter a valid number for height"
            binding.weightInputLayout.error = "Enter a valid number for weight"
        }
    }

    private fun displayBMIResult(bmi: Float, height: Float) {
        val correctedBmi = bmi.coerceIn(15f, 40f)
        bmiGauge.speedTo(correctedBmi, 2000)

        val bmiCategory = when {
            bmi < 16 -> "Severe Thinness"
            bmi < 17 -> "Moderate Thinness"
            bmi < 18.5 -> "Mild Thinness"
            bmi < 25 -> "Normal"
            bmi < 30 -> "Overweight"
            bmi < 35 -> "Obese Class I"
            bmi < 40 -> "Obese Class II"
            else -> "Obese Class III"
        }

        val healthyWeightMin = 18.5 * height.pow(2)
        val healthyWeightMax = 25 * height.pow(2)
        val resultText = "BMI = %.1f kg/m² (%s)".format(bmi, bmiCategory)
        binding.bmiValueText.text = resultText

        val healthyWeightRangeText = "Healthy weight range: %.1f kg - %.1f kg".format(healthyWeightMin, healthyWeightMax)
        binding.healthyWeightRangeText.text = healthyWeightRangeText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}