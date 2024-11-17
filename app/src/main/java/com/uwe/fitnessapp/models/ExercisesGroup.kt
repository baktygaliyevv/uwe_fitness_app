package com.uwe.fitnessapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExercisesGroup(
    val id: Int,
    val type: String,
    val image: String,
    val exercises: List<Exercise>
) : Parcelable {
    @Parcelize
    data class Exercise(
        val id: Int,
        val name: String,
        val description: String,
        val images: List<String>
    ) : Parcelable
}
