package com.uwe.fitnessapp.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentLogBinding
import com.uwe.fitnessapp.models.LogEntry
import com.uwe.fitnessapp.utils.ReadJSON
import com.uwe.fitnessapp.utils.readJSONFromFilesDir

class LogFragment : Fragment() {

    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogBinding.inflate(inflater, container, false)

        val logsJson = readJSONFromFilesDir(requireContext(), "logs.json")
        val logs: List<LogEntry> = Gson().fromJson(logsJson, object : TypeToken<List<LogEntry>>() {}.type)
        val sortedLogs = logs.sortedByDescending { it.date }

        adapter = LogAdapter(sortedLogs) { date ->
            val bundle = Bundle().apply { putString("date", date) }
            findNavController().navigate(R.id.navigation_exercise_log, bundle)
        }
        binding.recyclerViewLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLogs.adapter = adapter

        val divider = MaterialDividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
        }
        binding.recyclerViewLogs.addItemDecoration(divider)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

