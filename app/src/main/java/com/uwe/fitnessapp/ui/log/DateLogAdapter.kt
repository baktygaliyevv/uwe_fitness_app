package com.uwe.fitnessapp.ui.log

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.LogEntry

class DateLogAdapter(
    private val logs: MutableList<LogEntry>,
    private val exerciseName: String
) : RecyclerView.Adapter<DateLogAdapter.DateLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateLogViewHolder {
        // inflating the layout for each log date entry
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_log, parent, false)
        return DateLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateLogViewHolder, position: Int) {
        val log = logs[position]
        holder.dateText.text = log.date

        // clearing any previous sets before adding new ones
        holder.dataContainer.removeAllViews()

        // adding each set from the exercises under this log entry
        log.exercises.flatMap { it.sets }.forEach { set ->
            val setView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_set_log, holder.dataContainer, false)

            val weightText = setView.findViewById<TextView>(R.id.weightText)
            val repsText = setView.findViewById<TextView>(R.id.repsText)

            weightText.text = "${set.weight} kg"
            repsText.text = set.reps.toString()

            holder.dataContainer.addView(setView)
        }

        // allowing user to share their log via an intent
        holder.shareButton.setOnClickListener {
            val context = holder.itemView.context
            val shareText = generateShareText(log) // function to generate text for sharing

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            try {
                context.startActivity(Intent.createChooser(shareIntent, "Share Log"))
            } catch (e: Exception) {
                Toast.makeText(context, "No app available to share", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = logs.size

    fun updateLogs(newLogs: List<LogEntry>) {
        logs.clear()
        logs.addAll(newLogs)
        notifyDataSetChanged()
    }

    // formatting the log info for sharing purposes
    private fun generateShareText(log: LogEntry): String {
        val exercises = log.exercises.joinToString(separator = "\n") { exercise ->
            val sets = exercise.sets.joinToString(separator = "; ") { set ->
                "${set.reps} reps @ ${set.weight} kg"
            }
            "$exerciseName:\n$sets"
        }
        return "Workout log for ${log.date}:\n\n$exercises"
    }

    class DateLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val dataContainer: ViewGroup = itemView.findViewById(R.id.dataContainer)
        val shareButton: MaterialButton = itemView.findViewById(R.id.shareButton)
    }
}
