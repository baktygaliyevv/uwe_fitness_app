package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uwe.fitnessapp.databinding.FragmentTransitionBinding
import com.uwe.fitnessapp.models.ExerciseLog
import com.uwe.fitnessapp.models.LogEntry
import com.uwe.fitnessapp.models.SetLog
import com.uwe.fitnessapp.utils.getCurrentDate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import java.io.File

class TransitionFragment : Fragment() {

    private var _binding: FragmentTransitionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransitionBinding.inflate(inflater, container, false)

        val exerciseName = arguments?.getString("exerciseName") ?: "Exercise"
        val exerciseId = arguments?.getInt("exerciseId") ?: -1
        val exerciseGroupId = arguments?.getInt("exerciseGroupId") ?: -1

        binding.addLogButton.setOnClickListener {
            val weights = binding.weightsInput.text.toString().toFloatOrNull()
            val reps = binding.repsInput.text.toString().toIntOrNull()

            if (weights != null && reps != null) {
                saveLogData(exerciseGroupId, exerciseId, exerciseName, weights, reps)
                navigateToExerciseSets(exerciseGroupId, exerciseId, exerciseName)
            } else {
                if (weights == null) binding.weightsInput.error = "Please enter a valid weight"
                if (reps == null) binding.repsInput.error = "Please enter a valid number of reps"
            }
        }

        return binding.root
    }

    private fun saveLogData(
        exerciseGroupId: Int,
        exerciseId: Int,
        exerciseName: String,
        weights: Float,
        reps: Int
    ) {
        try {
            val file = File(requireContext().filesDir, "logs.json")
            if (!file.exists()) {
                file.writeText("[]")
            }

            val logsJson = file.readText()
            val logs: MutableList<LogEntry> = Gson().fromJson(
                logsJson, object : TypeToken<MutableList<LogEntry>>() {}.type
            )

            val currentDate = getCurrentDate()

            val dateLog = logs.find { it.date == currentDate } ?: LogEntry(currentDate, mutableListOf()).also {
                logs.add(it)
            }

            val exerciseLog = dateLog.exercises.find {
                it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId
            } ?: ExerciseLog(
                exercise_group_id = exerciseGroupId,
                exercise_id = exerciseId,
                sets = mutableListOf()
            ).also {
                dateLog.exercises.add(it)
            }

            exerciseLog.sets.add(SetLog(weights.toString(), reps))

            file.writeText(Gson().toJson(logs))

        } catch (e: Exception) {
            android.util.Log.e("SaveLogData", "Error: $e")
        }
    }


    private fun navigateToExerciseSets(groupId: Int, exerciseId: Int, exerciseName: String) {
        val bundle = Bundle().apply {
            putInt("exerciseGroupId", groupId)
            putInt("exerciseId", exerciseId)
            putString("exerciseName", exerciseName)
        }
        findNavController().navigate(R.id.navigation_exercise_sets, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
