package com.uwe.fitnessapp.ui.exercises.components

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentExerciseListBinding
import com.uwe.fitnessapp.models.ExercisesGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.utils.ReadJSON

class ExerciseListFragment : Fragment() {

    private var _binding: FragmentExerciseListBinding? = null
    private val binding get() = _binding!!
    private lateinit var exercisesData: ArrayList<ExercisesGroup?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseListBinding.inflate(inflater, container, false)

        val groupType = arguments?.getString("groupType")

        (activity as? AppCompatActivity)?.supportActionBar?.title = groupType

        val exercisesJson = ReadJSON(requireContext(), "exercises.json")
        exercisesData = Gson().fromJson(exercisesJson, object : TypeToken<ArrayList<ExercisesGroup?>>() {}.type)

        val selectedGroup = exercisesData.find { it?.type == groupType }

        enterTransition = MaterialFadeThrough().apply {
        }
        exitTransition = MaterialFadeThrough().apply {
        }

        selectedGroup?.exercises?.forEach { exercise ->
            val drawable = com.uwe.fitnessapp.utils.ReadImagesFromAssets(requireContext(), exercise.images[0])
            addCard(exercise.name, drawable) {
                findNavController().navigate(R.id.navigation_exercises_exercise, Bundle().apply {
                    putInt("exerciseGroupId", selectedGroup.id)
                    putInt("exerciseId", exercise.id)
                    putString("label", exercise.name)
                    putStringArray("images", exercise.images.toTypedArray())
                    putString("description", exercise.description)
                    putString("video", exercise.video)
                })
            }
        }

        return binding.root
    }

    private fun addCard(title: String, iconDrawable: Drawable?, onClickAction: () -> Unit) {
        val cardView = layoutInflater.inflate(R.layout.item_exercise_list, binding.cardsContainer, false)

        val titleTextView = cardView.findViewById<TextView>(R.id.exerciseItemTitle)
        val iconImageView = cardView.findViewById<ImageView>(R.id.exerciseItemImage)

        titleTextView.text = title
        iconImageView.setImageDrawable(iconDrawable)

        cardView.setOnClickListener { onClickAction() }

        binding.cardsContainer.addView(cardView)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
