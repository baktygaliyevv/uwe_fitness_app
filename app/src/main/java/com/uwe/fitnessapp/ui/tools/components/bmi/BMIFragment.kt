package com.uwe.fitnessapp.ui.tools.components.bmi

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.anastr.speedviewlib.SpeedView
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
            displayBMIResult(bmi)

        } catch (e: NumberFormatException) {
            binding.ageInputLayout.error = "Enter a valid number for age"
            binding.heightInputLayout.error = "Enter a valid number for height"
            binding.weightInputLayout.error = "Enter a valid number for weight"
        }
    }

    private fun displayBMIResult(bmi: Float) {
        bmiGauge.speedTo(bmi)

        val (color, bmiCategory) = when {
            bmi < 18.5 -> Color.YELLOW to "Underweight"
            bmi < 24.9 -> Color.GREEN to "Normal weight"
            bmi < 29.9 -> Color.YELLOW to "Overweight"
            else -> Color.RED to "Obesity"
        }

        val result = "BMI = %.1f kg/m² (%s)".format(bmi, bmiCategory)

        println(result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}