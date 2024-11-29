package com.uwe.fitnessapp.utils

import android.content.Context
import com.google.gson.Gson

fun SaveToJson(context: Context, fileName: String, data: Any) {
    val jsonString = Gson().toJson(data)
    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}
