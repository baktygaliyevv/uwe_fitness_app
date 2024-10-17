package com.uwe.fitnessapp.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolsViewModel =
            ViewModelProvider(this).get(ToolsViewModel::class.java)

        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnBmi.setOnClickListener {
            findNavController().navigate(R.id.navigation_bmi)
        }

        binding.btnCalories.setOnClickListener {
            findNavController().navigate(R.id.navigation_calories)
        }

        binding.btnHeartRate.setOnClickListener {
            findNavController().navigate(R.id.navigation_heart_rate)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
