package com.uwe.fitnessapp.ui.plans

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.databinding.FragmentPlansBinding
import com.uwe.fitnessapp.utils.ReadJSON
import com.google.android.material.card.MaterialCardView
import com.uwe.fitnessapp.utils.ReadImagesFromAssets

class PlansFragment : Fragment() {

    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!
    private lateinit var plansData: ArrayList<Map<String, Any>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Чтение данных планов из JSON файла
        val plansJson = ReadJSON(requireContext(), "plans.json")
        plansData = Gson().fromJson(plansJson, object : TypeToken<ArrayList<Map<String, Any>>>() {}.type)

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.root.findViewById(R.id.plansRecyclerView)
        val layoutManager = GridLayoutManager(context, 2)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 3 == 2) 2 else 1
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = PlansAdapter(plansData)
    }


    inner class PlansAdapter(private val plans: List<Map<String, Any>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return if (position % 3 == 2) TYPE_HORIZONTAL else TYPE_VERTICAL
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == TYPE_VERTICAL) {
                val view = inflater.inflate(R.layout.item_plan_vertical, parent, false)
                VerticalViewHolder(view)
            } else {
                val view = inflater.inflate(R.layout.item_plan_horizontal, parent, false)
                HorizontalViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val plan = plans[position]
            if (holder is VerticalViewHolder) {
                holder.bind(plan)
            } else if (holder is HorizontalViewHolder) {
                holder.bind(plan)
            }
        }

        override fun getItemCount(): Int = plans.size

        inner class VerticalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.planTitle)
            private val image: ImageView = itemView.findViewById(R.id.planImage)

            fun bind(plan: Map<String, Any>) {
                title.text = plan["name"] as String

                val imagePath = plan["image"] as String
                val drawable = ReadImagesFromAssets(itemView.context, imagePath)

                if (drawable != null) {
                    image.setImageDrawable(drawable)
                }
            }
        }

        inner class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.planTitle)
            private val image: ImageView = itemView.findViewById(R.id.planImage)

            fun bind(plan: Map<String, Any>) {
                title.text = plan["name"] as String

                val imagePath = plan["image"] as String
                val drawable = ReadImagesFromAssets(itemView.context, imagePath)

                if (drawable != null) {
                    image.setImageDrawable(drawable)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TYPE_VERTICAL = 0
        private const val TYPE_HORIZONTAL = 1
    }
}
