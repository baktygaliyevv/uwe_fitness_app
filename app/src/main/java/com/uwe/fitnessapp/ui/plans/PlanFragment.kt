package com.uwe.fitnessapp.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.Plan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.utils.readJSON
import com.uwe.fitnessapp.models.ExercisesGroup
import com.uwe.fitnessapp.utils.readImagesFromAssets

class PlanFragment : Fragment() {

    // stores the current plan passed as an argument
    private lateinit var plan: Plan

    // stores the exercise data loaded from JSON
    private lateinit var exercisesData: ArrayList<ExercisesGroup?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retrieve plan data from arguments passed to this fragment
        arguments?.let {
            plan = it.getParcelable("plan") ?: throw IllegalStateException("Plan must be provided")
        }

        // read and parse exercises.json to load exercise data
        val exercisesJson = readJSON(requireContext(), "exercises.json")
        exercisesData = Gson().fromJson(exercisesJson, object : TypeToken<ArrayList<ExercisesGroup?>>() {}.type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        // set up RecyclerView for displaying days of the plan
        val daysRecyclerView: RecyclerView = view.findViewById(R.id.daysRecyclerView)
        daysRecyclerView.layoutManager = LinearLayoutManager(context)

        // assign the adapter with the list of days
        val days = plan.days
        daysRecyclerView.adapter = DaysAdapter(days)

        // fade transition animations
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return view
    }

    // adapter for displaying the list of days in the plan
    inner class DaysAdapter(private val days: List<Plan.Day>) :
        RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

        // create the view holder for each day
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
            return DayViewHolder(view)
        }

        // bind day data to the view holder
        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            val day = days[position]
            holder.bind(day, position)
        }

        // get the number of days in the plan
        override fun getItemCount(): Int = days.size

        // view holder for a single day
        inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val dayTitle: TextView = itemView.findViewById(R.id.dayTitle)
            private val exercisesRecyclerView: RecyclerView = itemView.findViewById(R.id.exercisesRecyclerView)

            fun bind(day: Plan.Day, position: Int) {
                // set the title for the day (e.g., Day 1, Day 2)
                dayTitle.text = "Day ${position + 1}"
                val exercises = day.exercises

                // set up RecyclerView for the exercises within the day
                exercisesRecyclerView.layoutManager = LinearLayoutManager(context)
                exercisesRecyclerView.adapter = ExercisesAdapter(exercises)
            }
        }
    }

    // adapter for displaying the list of exercises within a day
    inner class ExercisesAdapter(private val exercises: List<Plan.ExerciseReference>) :
        RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

        // create the view holder for each exercise
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_list, parent, false)
            return ExerciseViewHolder(view)
        }

        // bind exercise data to the view holder
        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exerciseInfo = exercises[position]
            val exerciseGroupId = exerciseInfo.exerciseGroupId
            val exerciseId = exerciseInfo.exerciseId

            // find the specific exercise group and exercise by ID
            val selectedExerciseGroup = exercisesData.find { it?.id == exerciseGroupId }
            val selectedExercise = selectedExerciseGroup?.exercises?.find { it.id == exerciseId }

            // bind exercise details to the view
            holder.bind(selectedExercise)

            // handle click event to navigate to exercise details
            holder.itemView.setOnClickListener {
                if (selectedExercise != null) {
                    val bundle = Bundle().apply {
                        putString("label", selectedExercise.name)
                        putStringArray("images", selectedExercise.images.toTypedArray())
                        putString("description", selectedExercise.description)
                        putInt("exerciseGroupId", exerciseGroupId)
                        putInt("exerciseId", exerciseId)
                        putString("video", selectedExercise.video)
                        putString("source", "plans")
                    }
                    // navigate to the exercise details page
                    findNavController().navigate(R.id.navigation_exercises_exercise, bundle)
                }
            }
        }

        // get the number of exercises in the list
        override fun getItemCount(): Int = exercises.size

        // view holder for a single exercise
        inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val exerciseTitle: TextView = itemView.findViewById(R.id.exerciseItemTitle)
            private val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseItemImage)

            fun bind(exercise: ExercisesGroup.Exercise?) {
                if (exercise != null) {
                    // set the exercise title
                    exerciseTitle.text = exercise.name
                    // load and set the first image for the exercise
                    val drawable = readImagesFromAssets(itemView.context, exercise.images[0])
                    exerciseImage.setImageDrawable(drawable)
                }
            }
        }
    }
}
