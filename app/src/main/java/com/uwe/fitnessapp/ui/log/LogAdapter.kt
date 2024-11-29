package com.uwe.fitnessapp.ui.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.LogEntry

class LogAdapter(
    private val logs: List<LogEntry>,
    private val onDateClick: (String) -> Unit
) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log_date, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        holder.dateTextView.text = log.date
        holder.itemView.setOnClickListener {
            onDateClick(log.date)
        }
    }

    override fun getItemCount(): Int = logs.size

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: MaterialTextView = itemView.findViewById(R.id.text_date)
    }
}
