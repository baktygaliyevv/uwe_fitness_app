package com.uwe.fitnessapp.ui.tools.components.heartrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R

class HeartRateViewModel(private val zones: List<HeartRateFragment.HeartRateZone>) :
    RecyclerView.Adapter<HeartRateViewModel.HeartRateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_heart_rate_table, parent, false)
        return HeartRateViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        holder.bind(zones[position])

        val colorRes = when (position) {
            0 -> R.color.max_zone_color
            1 -> R.color.hard_zone_color
            2 -> R.color.moderate_zone_color
            3 -> R.color.light_zone_color
            4 -> R.color.very_light_zone_color
            else -> R.color.default_zone_color
        }
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, colorRes))
    }

    override fun getItemCount(): Int = zones.size

    inner class HeartRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val targetZone: TextView = itemView.findViewById(R.id.targetZone)
        private val intensity: TextView = itemView.findViewById(R.id.intensity)
        private val thrRange: TextView = itemView.findViewById(R.id.thrRange)

        fun bind(zone: HeartRateFragment.HeartRateZone) {
            targetZone.text = zone.targetZone
            intensity.text = zone.intensity
            thrRange.text = zone.thrRange
        }
    }
}
