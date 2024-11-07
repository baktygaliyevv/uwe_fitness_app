package com.uwe.fitnessapp.ui.exercises.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwe.fitnessapp.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        val images: Array<String>? = arguments?.getStringArray("images")

        if (images != null) {
            val viewPager = binding.viewPager
            val adapter = ExerciseImageAdapter(images.toList())
            viewPager.adapter = adapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
