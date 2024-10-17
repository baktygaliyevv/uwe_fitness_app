package com.uwe.fitnessapp.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)

        addCard(
            title = "BMI Calculator",
            description = "BMI Calculator will calculate your body mass index and give some recommendations to your diet",
            iconRes = R.drawable.ic_bmi_metabolism_black_24dp,
            onClickAction = { findNavController().navigate(R.id.navigation_bmi) }
        )

        addCard(
            title = "Calories Calculator",
            description = "Calculate your daily calorie intake depends on your goals: Gain Muscle Mass, Loose Fat, Weight maintenance / Recomposition",
            iconRes = R.drawable.ic_calories_restaurant_black_24dp,
            onClickAction = { findNavController().navigate(R.id.navigation_calories) }
        )

        addCard(
            title = "Resting Heart Rate",
            description = "Measure your normal resting heart rate based on your age, height, weight and give some recommendations",
            iconRes = R.drawable.ic_heart_rate_black_24dp,
            onClickAction = { findNavController().navigate(R.id.navigation_heart_rate) }
        )

        return binding.root
    }

    private fun addCard(title: String, description: String, iconRes: Int, onClickAction: () -> Unit) {
        val cardView = layoutInflater.inflate(R.layout.card_button, binding.cardsContainer, false)

        val titleTextView = cardView.findViewById<TextView>(R.id.titleText)
        val descriptionTextView = cardView.findViewById<TextView>(R.id.descriptionText)
        val iconImageView = cardView.findViewById<ImageView>(R.id.iconImage)

        titleTextView.text = title
        descriptionTextView.text = description
        iconImageView.setImageResource(iconRes)

        cardView.setOnClickListener { onClickAction() }

        binding.cardsContainer.addView(cardView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
