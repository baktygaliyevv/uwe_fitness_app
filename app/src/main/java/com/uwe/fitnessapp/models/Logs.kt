package com.uwe.fitnessapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogEntry(
    val date: String,
    val exercises: MutableList<ExerciseLog>
) : Parcelable

@Parcelize
data class ExerciseLog(
    val id: Int,
    val name: String,
    val sets: MutableList<SetLog>
) : Parcelable

@Parcelize
data class SetLog(
    val weight: String,
    val reps: Int
) : Parcelable