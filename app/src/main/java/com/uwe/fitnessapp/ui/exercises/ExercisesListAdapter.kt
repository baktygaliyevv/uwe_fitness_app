package com.uwe.fitnessapp.ui.exercises

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.uwe.fitnessapp.R
import com.uwe.fitnessapp.models.ExercisesGroup

class ExercisesListAdapter(context: Context, dataArrayList: ArrayList<ExercisesGroup?>?) :
    ArrayAdapter<ExercisesGroup?>(context, R.layout.fragment_exercise_item, dataArrayList!!) {
    @SuppressLint("DiscouragedApi")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val listData = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_exercise_item, parent, false)!!
        }
        val listImage = view!!.findViewById<ImageView>(R.id.exerciseItemImage)
        val listName = view.findViewById<TextView>(R.id.exerciseItemTitle)
//        val listTime = view.findViewById<TextView>(R.id.listTime)
        val drawableResourceId: Int =
            context.resources.getIdentifier(listData!!.image, "drawable", context.packageName)
        listImage.setImageResource(drawableResourceId)
        listName.text = listData.type
//        listTime.text = listData.time
        return view
    }
}