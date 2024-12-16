package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.databinding.FragmentExerciseLogBinding
import com.uwe.fitnessapp.utils.LogUtils

class ExerciseLogFragment : Fragment() {

    private var _binding: FragmentExerciseLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseLogBinding.inflate(inflater, container, false)

        val selectedDate = arguments?.getString("date") ?: return binding.root

        val logsJson = LogUtils.readLogs(requireContext())
        val selectedLog = logsJson.find { it.date == selectedDate }

        selectedLog?.let {
            val adapter = ExerciseLogAdapter(it.exercises)
            binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewExercises.adapter = adapter
        }

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


