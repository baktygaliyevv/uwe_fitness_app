package com.uwe.fitnessapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}
