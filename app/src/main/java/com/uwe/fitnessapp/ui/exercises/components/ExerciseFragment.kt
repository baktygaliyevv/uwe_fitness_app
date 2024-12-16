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

        // getting arguments passed to this fragment (images, description, video link)
        val images: Array<String>? = arguments?.getStringArray("images")
        val description = arguments?.getString("description")
        val videoUrl = arguments?.getString("video")

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        // if we have images, set up the viewpager and tab indicators
        if (images != null) {
            val viewPager = binding.viewPager
            val adapter = ExerciseImageAdapter(images.toList())
            viewPager.adapter = adapter

            val tabLayout = binding.tabLayout
            // attaching tabs to the viewpager, no text needed here since it's just an indicator
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        }

        // if we have a description, split it into muscle info and steps
        if (description != null) {
            val muscleText = description.substringBefore("\n\n")
            val stepsText = description.substringAfter("\n\n")

            binding.muscleTextView.text = muscleText
            binding.descriptionTextView.text = stepsText
        }

        // gathering exercise info for logging
        val exerciseGroupId = arguments?.getInt("exerciseGroupId") ?: -1
        val exerciseId = arguments?.getInt("exerciseId") ?: -1
        val exerciseName = arguments?.getString("label") ?: "Exercise"

        // when add log button is clicked, we'll decide which fragment to navigate to
        binding.addLogButton.setOnClickListener {
            handleAddLogClick(exerciseGroupId, exerciseId, exerciseName)
        }

        // if a video url is provided, tapping this button navigates to the video fragment
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
        // reading the logs file to see if this exercise already exists in the logs
        val logsJson = LogUtils.readLogs(requireContext())

        val exerciseExists = logsJson.any { logEntry ->
            logEntry.exercises.any { it.exercise_group_id == groupId && it.exercise_id == exerciseId }
        }

        // if it exists, navigate to the sets fragment to show past logs and add new sets
        // if not, we navigate to transition fragment to initially create and log this exercise
        val bundle = Bundle().apply {
            putInt("exerciseGroupId", groupId)
            putInt("exerciseId", exerciseId)
            putString("exerciseName", exerciseName)
        }

        if (exerciseExists) {
            findNavController().navigate(R.id.navigation_exercise_sets, bundle)
        } else {
            findNavController().navigate(R.id.navigation_transition, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
