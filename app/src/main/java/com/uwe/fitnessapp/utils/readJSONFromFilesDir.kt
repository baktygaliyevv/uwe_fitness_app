package com.uwe.fitnessapp.utils

import android.content.Context
import android.util.Log
import java.io.File

fun readJSONFromFilesDir(context: Context, path: String): String {
    val identifier = "[ReadJSON]"
    try {
        val file = File(context.filesDir, path)
        if (!file.exists()) {
            Log.e(identifier, "File not found: $path")
            return ""
        }
        return file.readText()
    } catch (e: Exception) {
        Log.e(identifier, "Error reading JSON: $e.")
        e.printStackTrace()
        return ""
    }
}
