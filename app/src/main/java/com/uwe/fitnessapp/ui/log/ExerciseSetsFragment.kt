package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.databinding.FragmentExerciseSetsBinding
import com.uwe.fitnessapp.models.ExerciseLog
import com.uwe.fitnessapp.models.LogEntry
import com.uwe.fitnessapp.models.SetLog
import com.uwe.fitnessapp.utils.LogUtils
import com.uwe.fitnessapp.utils.getCurrentDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExerciseSetsFragment : Fragment() {

    private var _binding: FragmentExerciseSetsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseSetsBinding.inflate(inflater, container, false)

        val exerciseName = arguments?.getString("exerciseName") ?: "Exercise"
        val exerciseGroupId = arguments?.getInt("exerciseGroupId") ?: -1
        val exerciseId = arguments?.getInt("exerciseId") ?: -1

        (activity as? AppCompatActivity)?.supportActionBar?.title = exerciseName

        val logs = LogUtils.readLogs(requireContext())

        val filteredLogs = logs.filter { log ->
            log.exercises.any { it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId }
        }.map { log ->
            LogEntry(
                log.date,
                log.exercises.filter { it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId }.toMutableList()
            )
        }.sortedByDescending { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd MMM yyyy")) }

        val dateLogAdapter = DateLogAdapter(filteredLogs)
        binding.recyclerViewSets.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSets.adapter = dateLogAdapter

        binding.addLogButton.setOnClickListener {
            val weights = binding.weightsInput.text.toString().toFloatOrNull()
            val reps = binding.repsInput.text.toString().toIntOrNull()

            if (weights != null && reps != null) {
                saveLogData(logs, exerciseGroupId, exerciseId, weights, reps)
                val updatedLogs = logs.filter { log ->
                    log.exercises.any { it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId }
                }.map { log ->
                    LogEntry(
                        log.date,
                        log.exercises.filter { it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId }
                            .toMutableList()
                    )
                }.sortedByDescending { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd MMM yyyy")) }

                dateLogAdapter.updateLogs(updatedLogs)
            } else {
                if (weights == null) binding.weightsInput.error = "Enter valid weight"
                if (reps == null) binding.weightsInput.error = "Enter valid reps"
            }
        }

        return binding.root
    }

    private fun saveLogData(
        logs: MutableList<LogEntry>, groupId: Int, exerciseId: Int, weights: Float, reps: Int
    ) {
        val currentDate = getCurrentDate()

        val dateLog = logs.find { it.date == currentDate } ?: LogEntry(currentDate, mutableListOf()).also {
            logs.add(it)
        }

        val exerciseLog = dateLog.exercises.find {
            it.exercise_group_id == groupId && it.exercise_id == exerciseId
        } ?: ExerciseLog(groupId, exerciseId, mutableListOf()).also {
            dateLog.exercises.add(it)
        }

        exerciseLog.sets.add(SetLog(weights.toString(), reps))
        LogUtils.writeLogs(requireContext(), logs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
