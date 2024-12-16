package com.uwe.fitnessapp.ui.exercises.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.utils.readImagesFromAssets

class ExerciseImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ExerciseImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // this imageview will show the exercise image we're currently on
        val imageView: ImageView = itemView.findViewById(R.id.exerciseImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating a simple layout containing just the imageview for an exercise
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_exercise_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // using custom function to load images from the assets folder
        val context = holder.itemView.context
        val drawable = readImagesFromAssets(context, images[position])
        holder.imageView.setImageDrawable(drawable)
    }

    override fun getItemCount() = images.size
}
