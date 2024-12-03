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
import com.uwe.fitnessapp.utils.ReadJSON
import com.uwe.fitnessapp.models.ExercisesGroup

class PlanFragment : Fragment() {

    private lateinit var plan: Plan
    private lateinit var exercisesData: ArrayList<ExercisesGroup?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            plan = it.getParcelable("plan") ?: throw IllegalStateException("Plan must be provided")
        }
        val exercisesJson = ReadJSON(requireContext(), "exercises.json")
        exercisesData = Gson().fromJson(exercisesJson, object : TypeToken<ArrayList<ExercisesGroup?>>() {}.type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        val daysRecyclerView: RecyclerView = view.findViewById(R.id.daysRecyclerView)
        daysRecyclerView.layoutManager = LinearLayoutManager(context)

        val days = plan.days
        daysRecyclerView.adapter = DaysAdapter(days)

        enterTransition = MaterialFadeThrough().apply {
        }
        exitTransition = MaterialFadeThrough().apply {
        }

        return view
    }

    inner class DaysAdapter(private val days: List<Plan.Day>) :
        RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
            return DayViewHolder(view)
        }

        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            val day = days[position]
            holder.bind(day, position)
        }

        override fun getItemCount(): Int = days.size

        inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val dayTitle: TextView = itemView.findViewById(R.id.dayTitle)
            private val exercisesRecyclerView: RecyclerView = itemView.findViewById(R.id.exercisesRecyclerView)

            fun bind(day: Plan.Day, position: Int) {
                dayTitle.text = "Day ${position + 1}"
                val exercises = day.exercises

                exercisesRecyclerView.layoutManager = LinearLayoutManager(context)
                exercisesRecyclerView.adapter = ExercisesAdapter(exercises)
            }
        }
    }

    inner class ExercisesAdapter(private val exercises: List<Plan.ExerciseReference>) :
        RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_list, parent, false)
            return ExerciseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exerciseInfo = exercises[position]
            val exerciseGroupId = exerciseInfo.exerciseGroupId
            val exerciseId = exerciseInfo.exerciseId

            val selectedExerciseGroup = exercisesData.find { it?.id == exerciseGroupId }
            val selectedExercise = selectedExerciseGroup?.exercises?.find { it.id == exerciseId }

            holder.bind(selectedExercise)
            holder.itemView.setOnClickListener {
                if (selectedExercise != null) {
                    val bundle = Bundle().apply {
                        putString("label", selectedExercise.name)
                        putStringArray("images", selectedExercise.images.toTypedArray())
                        putString("description", selectedExercise.description)
                        putInt("exerciseGroupId", exerciseGroupId)
                        putInt("exerciseId", exerciseId)
                    }
                    findNavController().navigate(R.id.navigation_exercises_exercise, bundle)
                }
            }
        }

        override fun getItemCount(): Int = exercises.size

        inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val exerciseTitle: TextView = itemView.findViewById(R.id.exerciseItemTitle)
            private val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseItemImage)

            fun bind(exercise: ExercisesGroup.Exercise?) {
                if (exercise != null) {
                    exerciseTitle.text = exercise.name
                    val drawable = com.uwe.fitnessapp.utils.ReadImagesFromAssets(itemView.context, exercise.images[0])
                    exerciseImage.setImageDrawable(drawable)
                }
            }
        }
    }

}
