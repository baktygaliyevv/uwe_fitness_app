package com.uwe.fitnessapp.ui.tools.components.calories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R

data class CalorieGoal(val description: String, val value: Int)

class CaloriesGoalAdapter(private val calorieGoals: List<CalorieGoal>) :
    RecyclerView.Adapter<CaloriesGoalAdapter.CalorieGoalViewHolder>() {


    inner class CalorieGoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.description)
        val value: TextView = view.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalorieGoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calorie_goal, parent, false)
        return CalorieGoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalorieGoalViewHolder, position: Int) {
        val goal = calorieGoals[position]
        holder.description.text = goal.description
        holder.value.text = "${goal.value} calories"
    }

    override fun getItemCount(): Int = calorieGoals.size
}
