package com.uwe.fitnessapp.ui.tools.components.heartrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwe.fitnessapp.databinding.FragmentHeartRateBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough

class HeartRateFragment : Fragment() {

    // binding for accessing views in the layout
    private var _binding: FragmentHeartRateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflate the layout and set up binding
        _binding = FragmentHeartRateBinding.inflate(inflater, container, false)

        // set a click listener on the calculate button
        binding.calculateButton.setOnClickListener {
            calculateHeartRate() // perform heart rate calculation
        }

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return binding.root
    }

    // data class to represent a heart rate zone
    data class HeartRateZone(
        val targetZone: String,   // name of the heart rate zone
        val intensity: String,    // intensity percentage
        val thrRange: String      // target heart rate range
    )

    // function to calculate heart rate zones based on the user's age
    private fun calculateHeartRate() {
        val ageText = binding.ageInput.text.toString() // retrieve user input for age

        // validate if age input is not empty
        if (ageText.isNotEmpty()) {
            try {
                val age = ageText.toInt() // convert input to integer
                if (age in 20..99) { // check if age is within the valid range
                    val maxHeartRate = 220 - age // calculate the maximum heart rate

                    // define heart rate zones with calculated target ranges
                    val zones = listOf(
                        HeartRateZone(
                            "Maximum\nVO2 Max Zone",
                            "90% - 100%",
                            "${(maxHeartRate * 0.9).toInt()} - ${maxHeartRate}"
                        ),
                        HeartRateZone(
                            "Hard\nAnaerobic Zone",
                            "80% - 90%",
                            "${(maxHeartRate * 0.8).toInt()} - ${(maxHeartRate * 0.9).toInt()}"
                        ),
                        HeartRateZone(
                            "Moderate\nAerobic Zone",
                            "70% - 80%",
                            "${(maxHeartRate * 0.7).toInt()} - ${(maxHeartRate * 0.8).toInt()}"
                        ),
                        HeartRateZone(
                            "Light\nFat Burn Zone",
                            "60% - 70%",
                            "${(maxHeartRate * 0.6).toInt()} - ${(maxHeartRate * 0.7).toInt()}"
                        ),
                        HeartRateZone(
                            "Very Light\nWarm Up Zone",
                            "50% - 60%",
                            "${(maxHeartRate * 0.5).toInt()} - ${(maxHeartRate * 0.6).toInt()}"
                        )
                    )

                    // set up RecyclerView to display the calculated zones
                    binding.heartRateTable.layoutManager = LinearLayoutManager(requireContext())
                    binding.heartRateTable.adapter = HeartRateViewModel(zones)

                    // clear any previous error messages
                    binding.ageInputLayout.error = null
                } else {
                    // show error if age is out of the valid range
                    binding.ageInputLayout.error = "Age should be between 20 and 99"
                }
            } catch (e: NumberFormatException) {
                // handle invalid input that cannot be converted to an integer
                binding.ageInputLayout.error = "Enter a valid number"
            }
        }
    }

    // clear the binding reference to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
