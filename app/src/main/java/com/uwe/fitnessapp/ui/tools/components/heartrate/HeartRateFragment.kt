package com.uwe.fitnessapp.ui.tools.components.heartrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwe.fitnessapp.databinding.FragmentHeartRateBinding

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

    private fun calculateHeartRate() {
        val ageText = binding.ageInput.text.toString()
        if (ageText.isNotEmpty()) {
            val age = ageText.toInt()

            val maxHeartRate = 220 - age

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
