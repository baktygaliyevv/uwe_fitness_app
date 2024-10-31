package com.uwe.fitnessapp.ui.tools.components.heartrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwe.fitnessapp.databinding.FragmentHeartRateBinding
import androidx.recyclerview.widget.LinearLayoutManager

class HeartRateFragment : Fragment() {

    private var _binding: FragmentHeartRateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeartRateBinding.inflate(inflater, container, false)

        binding.calculateButton.setOnClickListener {
            calculateHeartRate()
        }

        return binding.root
    }

    data class HeartRateZone(
        val targetZone: String,
        val intensity: String,
        val thrRange: String
    )

    private fun calculateHeartRate() {
        val ageText = binding.ageInput.text.toString()
        if (ageText.isNotEmpty()) {
            try {
                val age = ageText.toInt()
                if (age in 20..99) {
                    val maxHeartRate = 220 - age

                    val zones = listOf(
                        HeartRateZone(
                            "Maximum VO2 Max Zone",
                            "90% - 100%",
                            "${(maxHeartRate * 0.9).toInt()} - ${maxHeartRate}"
                        ),
                        HeartRateZone(
                            "Hard Anaerobic Zone",
                            "80% - 90%",
                            "${(maxHeartRate * 0.8).toInt()} - ${(maxHeartRate * 0.9).toInt()}"
                        ),
                        HeartRateZone(
                            "Moderate Aerobic Zone",
                            "70% - 80%",
                            "${(maxHeartRate * 0.7).toInt()} - ${(maxHeartRate * 0.8).toInt()}"
                        ),
                        HeartRateZone(
                            "Light Fat Burn Zone",
                            "60% - 70%",
                            "${(maxHeartRate * 0.6).toInt()} - ${(maxHeartRate * 0.7).toInt()}"
                        ),
                        HeartRateZone(
                            "Very Light Warm Up Zone",
                            "50% - 60%",
                            "${(maxHeartRate * 0.5).toInt()} - ${(maxHeartRate * 0.6).toInt()}"
                        )
                    )

                    binding.heartRateTable.layoutManager = LinearLayoutManager(requireContext())
                    binding.heartRateTable.adapter = HeartRateViewModel(zones)

                    binding.ageInputLayout.error = null
                }
                else {
                    binding.ageInputLayout.error = "Age should be between 20 and 99"
                }
            }
            catch (e: NumberFormatException) {
                binding.ageInputLayout.error = "Enter a valid number"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
