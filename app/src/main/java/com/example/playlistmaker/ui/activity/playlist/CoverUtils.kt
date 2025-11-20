package com.example.playlistmaker.ui.activity.playlist

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.net.toUri

private const val COVER_DIRECTORY = "playlist_covers"

fun saveCoverToInternalStorage(context: Context, sourceUri: Uri): String? {
    val directory = File(context.filesDir, COVER_DIRECTORY).apply {
        if (!exists()) {
            mkdirs()
        }
    }
    val file = File(directory, "playlist_cover_${System.currentTimeMillis()}.jpg")
    return try {
        context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        } ?: return null
        file.absolutePath
    } catch (e: IOException) {
        file.delete()
        null
    }
}

fun String.toCoverUri(): Uri {
    return if (startsWith("content://") || startsWith("file://")) {
        this.toUri()
    } else {
        Uri.fromFile(File(this))
    }
}

fun String.toCoverModel(): Any {
    return if (startsWith("content://") || startsWith("file://")) {
        this.toUri()
    } else {
        File(this)
    }
}

