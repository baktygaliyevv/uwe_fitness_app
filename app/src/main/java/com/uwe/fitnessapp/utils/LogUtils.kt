package com.uwe.fitnessapp.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.uwe.fitnessapp.models.LogEntry
import java.io.File

object LogUtils {

    private const val FILE_NAME = "logs.json"

    fun readLogs(context: Context): MutableList<LogEntry> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            file.writeText("[]") // Initialize an empty JSON array
        }

        return try {
            val logsJson = file.readText()
            Gson().fromJson(logsJson, object : TypeToken<MutableList<LogEntry>>() {}.type)
        } catch (e: JsonSyntaxException) {
            android.util.Log.e("LogUtils", "Malformed JSON. Resetting file: ${e.message}")
            file.writeText("[]")
            mutableListOf()
        }
    }

    fun writeLogs(context: Context, logs: MutableList<LogEntry>) {
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(Gson().toJson(logs))
    }

    fun printLogsJson(context: Context) {
        val file = File(context.filesDir, "logs.json")
        if (file.exists()) {
            val content = file.readText()
            Log.d("LogsJsonContent", content)
        } else {
            Log.d("LogsJsonContent", "logs.json not found")
        }
    }

}
