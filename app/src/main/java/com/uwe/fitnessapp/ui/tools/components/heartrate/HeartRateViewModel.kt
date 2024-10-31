package com.uwe.fitnessapp.ui.tools.components.heartrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.databinding.ItemHeartRateTableBinding

class HeartRateViewModel(private val zones: List<HeartRateFragment.HeartRateZone>) :
    RecyclerView.Adapter<HeartRateViewModel.HeartRateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        val binding = ItemHeartRateTableBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HeartRateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        holder.bind(zones[position])
    }

    override fun getItemCount(): Int = zones.size

    inner class HeartRateViewHolder(private val binding: ItemHeartRateTableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(zone: HeartRateFragment.HeartRateZone) {
            binding.targetZone.text = zone.targetZone
            binding.intensity.text = zone.intensity
            binding.thrRange.text = zone.thrRange
        }
    }
}
