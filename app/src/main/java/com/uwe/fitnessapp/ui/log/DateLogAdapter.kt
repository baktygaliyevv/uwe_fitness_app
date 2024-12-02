package com.uwe.fitnessapp.ui.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.LogEntry

class DateLogAdapter(
    private val logs: List<LogEntry>
) : RecyclerView.Adapter<DateLogAdapter.DateLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_log, parent, false)
        return DateLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateLogViewHolder, position: Int) {
        val log = logs[position]
        holder.dateText.text = log.date

        holder.dataContainer.removeAllViews()

        log.exercises.flatMap { it.sets }.forEach { set ->
            val setView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_set_log, holder.dataContainer, false)

            val weightText = setView.findViewById<TextView>(R.id.weightText)
            val repsText = setView.findViewById<TextView>(R.id.repsText)

            weightText.text = "${set.weight} kg"
            repsText.text = set.reps.toString()

            holder.dataContainer.addView(setView)
        }
    }

    override fun getItemCount(): Int = logs.size

    fun updateLogs(newLogs: List<LogEntry>) {
        (logs as MutableList).clear()
        logs.addAll(newLogs)
        notifyDataSetChanged()
    }

    class DateLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val dataContainer: ViewGroup = itemView.findViewById(R.id.dataContainer)
    }
}
