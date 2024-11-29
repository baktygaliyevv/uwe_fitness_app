package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.databinding.FragmentLogBinding
import com.uwe.fitnessapp.models.LogEntry
import com.uwe.fitnessapp.utils.ReadJSON

class LogFragment : Fragment() {

    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogBinding.inflate(inflater, container, false)

        val logsJson = ReadJSON(requireContext(), "logs.json")
        val logs: List<LogEntry> = Gson().fromJson(logsJson, object : TypeToken<List<LogEntry>>() {}.type)

        adapter = LogAdapter(logs) { date ->
            val bundle = Bundle().apply { putString("date", date) }
        }
        binding.recyclerViewLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLogs.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
