package com.uwe.fitnessapp.ui.exercises.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentExerciseBinding
import com.uwe.fitnessapp.utils.LogUtils

class ExerciseFragment : Fragment() {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        val images: Array<String>? = arguments?.getStringArray("images")
        val description = arguments?.getString("description")
        val videoUrl = arguments?.getString("video")

        enterTransition = MaterialFadeThrough().apply {
        }
        exitTransition = MaterialFadeThrough().apply {
        }

        if (images != null) {
            val viewPager = binding.viewPager
            val adapter = ExerciseImageAdapter(images.toList())
            viewPager.adapter = adapter

            val tabLayout = binding.tabLayout
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        }

        if (description != null) {
            val muscleText = description.substringBefore("\n\n")
            val stepsText = description.substringAfter("\n\n")

            binding.muscleTextView.text = muscleText
            binding.descriptionTextView.text = stepsText
        }
        val exerciseGroupId  = arguments?.getInt("exerciseGroupId") ?: -1
        val exerciseId = arguments?.getInt("exerciseId") ?: -1
        val exerciseName = arguments?.getString("label") ?: "Exercise"

        binding.addLogButton.setOnClickListener {
            handleAddLogClick(exerciseGroupId, exerciseId, exerciseName)
        }

        binding.playVideoButton.setOnClickListener {
            if (!videoUrl.isNullOrEmpty()) {
                val bundle = Bundle().apply {
                    putString("video", videoUrl)
                }
                findNavController().navigate(R.id.navigation_video, bundle)
            }
        }
        return binding.root

    }
    private fun handleAddLogClick(groupId: Int, exerciseId: Int, exerciseName: String) {
        // Read logs.json to check if the exercise already exists
        val logsJson = LogUtils.readLogs(requireContext())

        val exerciseExists = logsJson.any { logEntry ->
            logEntry.exercises.any { it.exercise_group_id == groupId && it.exercise_id == exerciseId }
        }

        if (exerciseExists) {
            // If the exercise exists, navigate to ExerciseSetsFragment
            val bundle = Bundle().apply {
                putInt("exerciseGroupId", groupId)
                putInt("exerciseId", exerciseId)
                putString("exerciseName", exerciseName)
            }
            findNavController().navigate(R.id.navigation_exercise_sets, bundle)
        } else {
            // If the exercise does not exist, navigate to TransitionFragment
            val bundle = Bundle().apply {
                putInt("exerciseGroupId", groupId)
                putInt("exerciseId", exerciseId)
                putString("exerciseName", exerciseName)
            }
            findNavController().navigate(R.id.navigation_transition, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
