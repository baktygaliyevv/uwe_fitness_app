package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

        // setting the action bar title to the current exercise name
        val exerciseName = arguments?.getString("exerciseName") ?: "Exercise"
        val exerciseGroupId = arguments?.getInt("exerciseGroupId") ?: -1
        val exerciseId = arguments?.getInt("exerciseId") ?: -1
        (activity as? AppCompatActivity)?.supportActionBar?.title = exerciseName

        // reading logs and filtering them by the given exercise
        val logs = LogUtils.readLogs(requireContext()).toMutableList()
        val filteredLogs = logs.filter { log ->
            log.exercises.any { it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId }
        }.map { log ->
            LogEntry(
                log.date,
                log.exercises.filter { it.exercise_group_id == exerciseGroupId && it.exercise_id == exerciseId }.toMutableList()
            )
        }.sortedByDescending { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd MMM yyyy")) }.toMutableList()

        // setting up the recycler view adapter to display sets by date
        val dateLogAdapter = DateLogAdapter(filteredLogs, exerciseName)
        binding.recyclerViewSets.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSets.adapter = dateLogAdapter

        // handling the add button click to save new sets to logs
        binding.addLogButton.setOnClickListener {
            val weights = binding.weightsInput.text.toString().toFloatOrNull()
            val reps = binding.repsInput.text.toString().toIntOrNull()

            if (weights != null && reps != null) {
                saveLogData(logs, exerciseGroupId, exerciseId, weights, reps)

                // after saving, updating the adapter with the newly added logs
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
                // showing errors if the user inputs invalid data
                if (weights == null) binding.weightsInput.error = "Enter valid weight"
                if (reps == null) binding.weightsInput.error = "Enter valid reps"
            }
        }

        return binding.root
    }

    // saving the newly added log to an existing date or creating a new one if needed
    private fun saveLogData(
        logs: MutableList<LogEntry>, groupId: Int, exerciseId: Int, weights: Float, reps: Int
    ) {
        val currentDate = getCurrentDate()

        // find current date entry or create it if doesn't exist
        val dateLog = logs.find { it.date == currentDate } ?: LogEntry(currentDate, mutableListOf()).also {
            logs.add(it)
        }

        // find the exercise log within today's date or create it if doesn't exist
        val exerciseLog = dateLog.exercises.find {
            it.exercise_group_id == groupId && it.exercise_id == exerciseId
        } ?: ExerciseLog(groupId, exerciseId, mutableListOf()).also {
            dateLog.exercises.add(it)
        }

        // add the new set and write logs back to storage
        exerciseLog.sets.add(SetLog(weights.toString(), reps))
        LogUtils.writeLogs(requireContext(), logs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
