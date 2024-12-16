package com.uwe.fitnessapp.ui.tools.components.heartrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R

// Adapter for displaying heart rate zones in a RecyclerView
class HeartRateViewModel(private val zones: List<HeartRateFragment.HeartRateZone>) :
    RecyclerView.Adapter<HeartRateViewModel.HeartRateViewHolder>() {

    // Create a new ViewHolder instance by inflating the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_heart_rate_table, parent, false) // inflate the item layout
        return HeartRateViewHolder(view)
    }

    // Bind data to the ViewHolder for each position
    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        holder.bind(zones[position]) // bind data for the specific zone

        // dynamically set the background color based on position
        val colorRes = when (position) {
            0 -> R.color.max_zone_color       // VO2 Max Zone
            1 -> R.color.hard_zone_color      // Anaerobic Zone
            2 -> R.color.moderate_zone_color  // Aerobic Zone
            3 -> R.color.light_zone_color     // Fat Burn Zone
            4 -> R.color.very_light_zone_color // Warm Up Zone
            else -> R.color.default_zone_color
        }
        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, colorRes)
        )
    }

    // Return the total count of zones to display
    override fun getItemCount(): Int = zones.size

    // ViewHolder class for holding and managing views for each item
    inner class HeartRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val targetZone: TextView = itemView.findViewById(R.id.targetZone) // zone name
        private val intensity: TextView = itemView.findViewById(R.id.intensity)   // intensity range
        private val thrRange: TextView = itemView.findViewById(R.id.thrRange)     // target heart rate range

        // bind data to the TextViews
        fun bind(zone: HeartRateFragment.HeartRateZone) {
            targetZone.text = zone.targetZone
            intensity.text = zone.intensity
            thrRange.text = zone.thrRange
        }
    }
}
