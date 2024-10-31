package com.uwe.fitnessapp.models

data class ExercisesGroup(
    val type: String,
    val image: String,
    val exercises: List<Exercise>
) {
    data class Exercise(
        val name: String,
        val description: String,
        val images: List<String>
    )
}