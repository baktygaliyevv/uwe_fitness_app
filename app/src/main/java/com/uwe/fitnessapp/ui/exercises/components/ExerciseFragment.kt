package com.uwe.fitnessapp.ui.exercises.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentExerciseBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
