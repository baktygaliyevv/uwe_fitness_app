package com.uwe.fitnessapp.ui.exercises

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentExercisesBinding
import com.uwe.fitnessapp.models.ExercisesGroup
import com.uwe.fitnessapp.utils.ReadJSON

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private lateinit var exercisesData: ArrayList<ExercisesGroup?>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exercisesJson = ReadJSON(requireContext(), "exercises.json")
        exercisesData = Gson().fromJson(exercisesJson, object : TypeToken<ArrayList<ExercisesGroup?>>() {})

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        for (group in exercisesData) {
            val drawableResourceId: Int = resources.getIdentifier(group!!.image, "drawable", activity?.packageName)
            addCard(group.type, drawableResourceId, {
                val nav = findNavController()
                nav.navigate(R.id.navigation_exercises_exercise, Bundle().apply {
                    putString("label", group.type)
                })
            })
        }

        return root
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