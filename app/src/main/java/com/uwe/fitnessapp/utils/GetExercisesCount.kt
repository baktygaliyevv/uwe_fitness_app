package com.uwe.fitnessapp.utils

import com.uwe.fitnessapp.models.ExercisesGroup

fun GetExercisesCount(exercisesData: ArrayList<ExercisesGroup?>, type: String): Int {
    return exercisesData.find { it?.type == type }?.exercises?.size ?: 0
}
