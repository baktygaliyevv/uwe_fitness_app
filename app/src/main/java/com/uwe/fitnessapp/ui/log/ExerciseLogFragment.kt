package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.R
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
            val adapter = ExerciseLogAdapter(it.exercises) { exercise ->
                val bundle = Bundle().apply {
                    putInt("exerciseGroupId", exercise.exercise_group_id)
                    putInt("exerciseId", exercise.exercise_id)
                }
                findNavController().navigate(R.id.navigation_exercise_sets, bundle)
            }
            binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewExercises.adapter = adapter
        }
        enterTransition = MaterialFadeThrough().apply {
        }
        exitTransition = MaterialFadeThrough().apply {
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


