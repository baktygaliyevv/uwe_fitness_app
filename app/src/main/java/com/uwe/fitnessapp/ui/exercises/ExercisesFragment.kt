package com.uwe.fitnessapp.ui.exercises

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentExercisesBinding
import com.uwe.fitnessapp.models.ExercisesGroup
import com.uwe.fitnessapp.utils.readJSON
import com.uwe.fitnessapp.utils.getExercisesCount
import com.uwe.fitnessapp.utils.readImagesFromAssets

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private lateinit var exercisesData: ArrayList<ExercisesGroup?>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // reading exercises data from a local json file
        val exercisesJson = readJSON(requireContext(), "exercises.json")
        exercisesData = Gson().fromJson(exercisesJson, object : TypeToken<ArrayList<ExercisesGroup?>>() {}.type)

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // creating a card for each exercise group, showing its image, title and exercise count
        for (group in exercisesData) {
            val drawable = readImagesFromAssets(requireContext(), group!!.image)
            val exerciseCount = getExercisesCount(exercisesData, group.type)
            addCard(group.type, drawable, exerciseCount) {
                // on click, navigate to the specific exercise list for that group
                findNavController().navigate(R.id.navigation_exercises_list, Bundle().apply {
                    putString("groupType", group.type)
                })
            }
        }

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return root
    }

    // inflating a card layout, setting its title, image, and count, then adding it to the container
    private fun addCard(title: String, iconDrawable: Drawable?, exerciseCount: Int, onClickAction: () -> Unit) {
        val cardView = layoutInflater.inflate(R.layout.fragment_exercise_item, binding.cardsContainer, false)

        val countTextView = cardView.findViewById<TextView>(R.id.exerciseItemCount)
        val titleTextView = cardView.findViewById<TextView>(R.id.exerciseItemTitle)
        val iconImageView = cardView.findViewById<ImageView>(R.id.exerciseItemImage)

        titleTextView.text = title
        countTextView.text = "$exerciseCount exercises"
        iconImageView.setImageDrawable(iconDrawable)

        cardView.setOnClickListener { onClickAction() }

        binding.cardsContainer.addView(cardView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
