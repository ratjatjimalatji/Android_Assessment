package com.glucode.about_you.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LocalStorageManager(private val context: Context) {
    private val IMAGES_DIR = "profile_pictures"

    init {
        createImageDirectory()
    }

    private fun createImageDirectory() {
        File(context.filesDir, IMAGES_DIR).also {
            if (!it.exists()) {
                it.mkdir()
            }
        }
    }

    fun saveProfilePicture(engineerId: String, bitmap: Bitmap): Boolean {
        return try {
            val file = File(context.filesDir, "$IMAGES_DIR/${engineerId}.jpg")
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun loadProfilePicture(engineerId: String): Bitmap? {
        val file = File(context.filesDir, "$IMAGES_DIR/${engineerId}.jpg")
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    fun deleteProfilePicture(engineerId: String): Boolean {
        val file = File(context.filesDir, "$IMAGES_DIR/${engineerId}.jpg")
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}