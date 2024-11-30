package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentExerciseLogBinding
import com.uwe.fitnessapp.models.LogEntry
import com.uwe.fitnessapp.utils.ReadJSON
import com.uwe.fitnessapp.utils.readJSONFromFilesDir

class ExerciseLogFragment : Fragment() {

    private var _binding: FragmentExerciseLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseLogBinding.inflate(inflater, container, false)

        val selectedDate = arguments?.getString("date") ?: return binding.root

        val logsJson = readJSONFromFilesDir(requireContext(), "logs.json")
        val logs: List<LogEntry> = Gson().fromJson(logsJson, object : TypeToken<List<LogEntry>>() {}.type)
        val selectedLog = logs.find { it.date == selectedDate }

        selectedLog?.let {
            val adapter = ExerciseLogAdapter(it.exercises) { exercise ->
                val bundle = Bundle().apply {
                    putParcelable("exerciseLog", exercise)
                }
                findNavController().navigate(R.id.navigation_transition, bundle)
            }
            binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewExercises.adapter = adapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


