package com.uwe.fitnessapp.ui.exercises.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        selectedGroup?.exercises?.forEach { exercise ->
            val drawableResourceId: Int = resources.getIdentifier(exercise.images[0], "drawable", activity?.packageName)
            addCard(exercise.name, drawableResourceId) {
                findNavController().navigate(R.id.navigation_exercises_exercise, Bundle().apply {
                    putString("label", exercise.name)
                    putStringArray("images", exercise.images.toTypedArray())
                    putString("description", exercise.description)
                })
            }
        }

        return binding.root
    }


    private fun addCard(title: String, iconRes: Int, onClickAction: () -> Unit) {
        val cardView = layoutInflater.inflate(R.layout.fragment_exercise_item, binding.cardsContainer, false)

        val titleTextView = cardView.findViewById<TextView>(R.id.exerciseItemTitle)
        val iconImageView = cardView.findViewById<ImageView>(R.id.exerciseItemImage)

        titleTextView.text = title
        iconImageView.setImageResource(iconRes)

        cardView.setOnClickListener { onClickAction() }

        binding.cardsContainer.addView(cardView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
