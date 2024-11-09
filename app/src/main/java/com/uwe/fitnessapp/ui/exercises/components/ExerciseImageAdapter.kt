package com.uwe.fitnessapp.ui.exercises.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R

class ExerciseImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ExerciseImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.exerciseImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_exercise_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val imageResourceId = context.resources.getIdentifier(images[position], "drawable", context.packageName)
        holder.imageView.setImageResource(imageResourceId)
    }

    override fun getItemCount() = images.size
}
