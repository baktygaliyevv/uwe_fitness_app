package com.uwe.fitnessapp.ui.log

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.ExerciseLog
import android.widget.ImageView
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.models.ExercisesGroup
import com.uwe.fitnessapp.utils.ReadJSON

class ExerciseLogAdapter(
    private val exercises: List<ExerciseLog>,
    private val onExerciseClick: (ExerciseLog) -> Unit
) : RecyclerView.Adapter<ExerciseLogAdapter.ExerciseLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_list, parent, false)
        return ExerciseLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseLogViewHolder, position: Int) {
        val exercise = exercises[position]
        val loadedExercise = findExerciseById(holder.itemView.context, exercise.exercise_group_id, exercise.exercise_id)

        holder.exerciseTitle.text = loadedExercise?.name ?: "Unknown Exercise"

        val imagePath = loadedExercise?.images?.getOrNull(0)
        if (imagePath != null) {
            val drawable = com.uwe.fitnessapp.utils.ReadImagesFromAssets(holder.itemView.context, imagePath)
            holder.exerciseImage.setImageDrawable(drawable)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("exerciseGroupId", exercise.exercise_group_id)
                putInt("exerciseId", exercise.exercise_id)
                putString("exerciseName", loadedExercise?.name ?: "Exercise")
            }
            holder.itemView.findNavController().navigate(R.id.navigation_exercise_sets, bundle)
        }
    }

    override fun getItemCount(): Int = exercises.size

    class ExerciseLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseTitle: MaterialTextView = itemView.findViewById(R.id.exerciseItemTitle)
        val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseItemImage)
    }

    private fun findExerciseById(context: Context, groupId: Int, exerciseId: Int): ExercisesGroup.Exercise? {
        val exercisesJson = ReadJSON(context, "exercises.json")
        val exerciseGroups: List<ExercisesGroup> = Gson().fromJson(
            exercisesJson, object : TypeToken<List<ExercisesGroup>>() {}.type
        )
        val group = exerciseGroups.find { it.id == groupId }
        return group?.exercises?.find { it.id == exerciseId }
    }
}



