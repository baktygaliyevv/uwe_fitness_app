package com.uwe.fitnessapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExercisesGroup(
    val type: String,
    val image: String,
    val exercises: List<Exercise>
) : Parcelable {
    @Parcelize
    data class Exercise(
        val name: String,
        val description: String,
        val images: List<String>
    ) : Parcelable
}
