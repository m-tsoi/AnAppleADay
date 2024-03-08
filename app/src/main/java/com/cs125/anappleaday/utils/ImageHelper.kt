package com.cs125.anappleaday.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.UUID

class ImageHelper {
    companion object {
        fun bitmapToUri(bitmap: Bitmap, cacheDir: File?): Uri {
            val file = File(cacheDir, "${UUID.randomUUID()}.jpeg")
            try {
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return Uri.fromFile(file)
        }

        fun uploadImage(context: Context, dir: String, storageRef: StorageReference, uri: Uri): Task<Uri> {
            val filePath = "${dir}/${UUID.randomUUID()}"
            val fileRef = storageRef.child(filePath)
            val uploadTask = fileRef.putFile(uri)
            Log.d("Avatar", uri.toString())
            return uploadTask.continueWithTask {
                    task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        Toast.makeText(context,"${it}", Toast.LENGTH_SHORT).show()
                        throw it
                    }
                }
                fileRef.downloadUrl
            }
        }
    }
}