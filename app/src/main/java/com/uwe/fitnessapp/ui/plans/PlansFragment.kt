package com.uwe.fitnessapp.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentPlansBinding
import com.uwe.fitnessapp.utils.readJSON
import com.uwe.fitnessapp.models.Plan
import com.uwe.fitnessapp.utils.readImagesFromAssets

class PlansFragment : Fragment() {

    // view binding for this fragment
    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!

    // list of plans parsed from the JSON
    private lateinit var plansData: ArrayList<Map<String, Any>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialize view binding
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // read and parse JSON file containing plans
        val plansJson = readJSON(requireContext(), "plans.json")
        plansData = Gson().fromJson(plansJson, object : TypeToken<ArrayList<Map<String, Any>>>() {}.type)

        // set up RecyclerView with plans data
        setupRecyclerView()

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return root
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.root.findViewById(R.id.plansRecyclerView)

        // use GridLayoutManager to display plans in a grid layout
        val layoutManager = GridLayoutManager(context, 2)

        // adjust the span size for the third item in each row
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 3 == 2) 2 else 1
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = PlansAdapter(plansData)

        // disable default animations for smoother transitions
        recyclerView.itemAnimator = null
    }

    // Adapter for displaying plans in the RecyclerView
    inner class PlansAdapter(private val plans: List<Map<String, Any>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            // determine the view type: vertical or horizontal
            return if (position % 3 == 2) TYPE_HORIZONTAL else TYPE_VERTICAL
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == TYPE_VERTICAL) {
                // create a vertical view holder
                val view = inflater.inflate(R.layout.item_plan_vertical, parent, false)
                VerticalViewHolder(view)
            } else {
                // create a horizontal view holder
                val view = inflater.inflate(R.layout.item_plan_horizontal, parent, false)
                HorizontalViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // bind the plan data based on view type
            val plan = plans[position]
            if (holder is VerticalViewHolder) {
                holder.bind(plan)
            } else if (holder is HorizontalViewHolder) {
                holder.bind(plan)
            }
        }

        override fun getItemCount(): Int = plans.size

        // ViewHolder for vertical plan items
        inner class VerticalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.planTitle)
            private val image: ImageView = itemView.findViewById(R.id.planImage)

            fun bind(plan: Map<String, Any>) {
                // set plan title
                title.text = plan["name"] as String

                // load plan image from assets
                val imagePath = plan["image"] as String
                val drawable = readImagesFromAssets(itemView.context, imagePath)
                if (drawable != null) {
                    image.setImageDrawable(drawable)
                }

                // navigate to plan details page when clicked
                itemView.setOnClickListener {
                    val id = (plan["id"] as Double).toInt()
                    val name = plan["name"] as String
                    val days = plan["days"] as List<Map<String, Any>>
                    val convertedDays = days.map { day ->
                        Plan.Day(
                            id = (day["id"] as Double).toInt(),
                            exercises = (day["exercises"] as List<Map<String, Any>>).map { exercise ->
                                Plan.ExerciseReference(
                                    exerciseGroupId = (exercise["exercise_group_id"] as Double).toInt(),
                                    exerciseId = (exercise["exercise_id"] as Double).toInt()
                                )
                            }
                        )
                    }
                    val planObject = Plan(id = id, name = name, image = imagePath, days = convertedDays)

                    val bundle = Bundle().apply {
                        putParcelable("plan", planObject)
                        putString("planName", name)
                    }
                    findNavController().navigate(R.id.navigation_plan_detail, bundle)
                }
            }
        }

        // ViewHolder for horizontal plan items
        inner class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.planTitle)
            private val image: ImageView = itemView.findViewById(R.id.planImage)

            fun bind(plan: Map<String, Any>) {
                // set plan title
                title.text = plan["name"] as String

                // load plan image from assets
                val imagePath = plan["image"] as String
                val drawable = readImagesFromAssets(itemView.context, imagePath)
                if (drawable != null) {
                    image.setImageDrawable(drawable)
                }

                // navigate to plan details page when clicked
                itemView.setOnClickListener {
                    val id = (plan["id"] as Double).toInt()
                    val name = plan["name"] as String
                    val days = plan["days"] as List<Map<String, Any>>
                    val convertedDays = days.map { day ->
                        Plan.Day(
                            id = (day["id"] as Double).toInt(),
                            exercises = (day["exercises"] as List<Map<String, Any>>).map { exercise ->
                                Plan.ExerciseReference(
                                    exerciseGroupId = (exercise["exercise_group_id"] as Double).toInt(),
                                    exerciseId = (exercise["exercise_id"] as Double).toInt()
                                )
                            }
                        )
                    }
                    val planObject = Plan(id = id, name = name, image = imagePath, days = convertedDays)

                    val bundle = Bundle().apply {
                        putParcelable("plan", planObject)
                        putString("planName", name)
                    }
                    findNavController().navigate(R.id.navigation_plan_detail, bundle)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clean up view binding to prevent memory leaks
        _binding = null
    }

    companion object {
        private const val TYPE_VERTICAL = 0
        private const val TYPE_HORIZONTAL = 1
    }
}
