package com.uwe.fitnessapp.utils

import android.content.Context
import com.google.gson.Gson
import java.io.File

fun SaveToJson(context: Context, path: String, data: Any) {
    try {
        val file = File(context.filesDir, path)
        val jsonString = Gson().toJson(data)
        file.writeText(jsonString)
    } catch (e: Exception) {
        android.util.Log.e("[SaveToJson]", "Error saving JSON: $e.")
        e.printStackTrace()
    }
}
