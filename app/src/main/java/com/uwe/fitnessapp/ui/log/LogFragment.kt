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
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentLogBinding
import com.uwe.fitnessapp.utils.LogUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LogFragment : Fragment() {

    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogBinding.inflate(inflater, container, false)

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        // reading and sorting logs by date so the newest entries show up first
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val logsJson = LogUtils.readLogs(requireContext())
        val sortedLogs = logsJson.sortedByDescending { LocalDate.parse(it.date, dateFormatter) }

        // setting up the adapter so clicking a date goes to detailed logs for that day
        adapter = LogAdapter(sortedLogs) { date ->
            val bundle = Bundle().apply { putString("date", date) }
            findNavController().navigate(R.id.navigation_exercise_log, bundle)
        }

        // using a linear layout to list logs and adding a divider to separate them clearly
        binding.recyclerViewLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLogs.adapter = adapter
        binding.recyclerViewLogs.itemAnimator = null

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
