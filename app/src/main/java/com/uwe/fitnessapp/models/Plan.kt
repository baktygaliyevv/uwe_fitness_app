package com.uwe.fitnessapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plan(
    val id: Int,
    val name: String,
    val image: String,
    val days: List<Day>
) : Parcelable {
    @Parcelize
    data class Day(
        val id: Int,
        val exercises: List<ExerciseReference>
    ) : Parcelable

    @Parcelize
    data class ExerciseReference(
        val exerciseGroupId: Int,
        val exerciseId: Int
    ) : Parcelable
}
