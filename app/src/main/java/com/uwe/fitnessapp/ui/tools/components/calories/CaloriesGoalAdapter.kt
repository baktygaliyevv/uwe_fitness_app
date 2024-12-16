package com.uwe.fitnessapp.ui.tools.components.calories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.CalorieGoal

// adapter for displaying calorie goals in a RecyclerView
class CaloriesGoalAdapter(private val calorieGoals: List<CalorieGoal>) :
    RecyclerView.Adapter<CaloriesGoalAdapter.CalorieGoalViewHolder>() {

    // view holder class that holds the views for each item
    inner class CalorieGoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // references to the views inside the item layout
        val description: TextView = view.findViewById(R.id.description) // displays goal description
        val value: TextView = view.findViewById(R.id.value) // displays calorie value
    }

    // creates new view holders when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalorieGoalViewHolder {
        // inflate the layout for an individual item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calorie_goal, parent, false)
        return CalorieGoalViewHolder(view)
    }

    // binds data to the views for each item
    override fun onBindViewHolder(holder: CalorieGoalViewHolder, position: Int) {
        // get the calorie goal at the current position
        val goal = calorieGoals[position]

        // bind the goal data to the corresponding views
        holder.description.text = goal.description
        holder.value.text = "${goal.value} calories"
    }

    // returns the total number of items in the list
    override fun getItemCount(): Int = calorieGoals.size
}
