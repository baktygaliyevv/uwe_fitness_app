package com.uwe.fitnessapp.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.databinding.FragmentExercisesBinding
import com.uwe.fitnessapp.models.ExercisesGroup
import com.uwe.fitnessapp.utils.ReadJSON

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private lateinit var listAdapter: ExercisesListAdapter
    private lateinit var exercisesData: ArrayList<ExercisesGroup?>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exercisesJson = ReadJSON(requireContext(), "exercises.json")
        exercisesData = Gson().fromJson(exercisesJson, object : TypeToken<ArrayList<ExercisesGroup?>>() {})

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listAdapter = ExercisesListAdapter(requireContext(), exercisesData)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}