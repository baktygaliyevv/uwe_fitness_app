package com.uwe.fitnessapp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.InputStream

fun ReadImagesFromAssets(context: Context, imagePath: String): Drawable? {
    val identifier = "[ReadImagesFromAssets]"
    return try {
        val inputStream: InputStream = context.assets.open(imagePath)
        Drawable.createFromStream(inputStream, null)
    } catch (e: Exception) {
        Log.e(identifier, "Error reading image from assets: $e.")
        e.printStackTrace()
        null
    }
}
