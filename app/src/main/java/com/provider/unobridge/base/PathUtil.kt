package com.provider.unobridge.base

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.net.URISyntaxException


object PathUtil {



    private const val LOG_FOLDER_NAME = "log"
    private const val RTC_LOG_FILE_NAME = "agora-rtc.log"
    private const val RTM_LOG_FILE_NAME = "agora-rtm.log"


    fun rtcLogFile(context: Context): String? {
        return logFilePath(context, RTC_LOG_FILE_NAME)
    }

    fun rtmLogFile(context: Context): String? {
        return logFilePath(context, RTM_LOG_FILE_NAME)
    }

    private fun logFilePath(context: Context, name: String): String? {
        var folder: File?
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q) {
            folder =
                File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), LOG_FOLDER_NAME)
        } else {
            val path = Environment.getExternalStorageDirectory()
                .absolutePath + File.separator +
                    context.packageName + File.separator +
                    name
            folder = File(path)
            if (!folder.exists() && !folder.mkdir()) folder = null
        }
        return if (folder != null && !folder.exists() && !folder.mkdir()) "" else File(folder,
            RTC_LOG_FILE_NAME
        ).absolutePath
    }

    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
// deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.applicationContext, uri)
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                var id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }

                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]

                Log.e("typee", type)
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection =
                arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun rotateImage(
        context: Context,
        galleryBitmap: Bitmap,
        contentUri: Uri
    ): Bitmap {
        var ei: ExifInterface? = null
        try {
            val file = File(getPath(context, contentUri))
            ei = ExifInterface(file.absolutePath)
        } catch (e: Exception) {
//            ToastUtils.makeToast(e.message)
        }
        val orientation = ei!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotatePicture(
                galleryBitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotatePicture(
                galleryBitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotatePicture(
                galleryBitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> galleryBitmap
            else -> galleryBitmap
        }
    }

    fun rotateImage(galleryBitmap: Bitmap, file: File): Bitmap {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(file.absolutePath)
        } catch (e: Exception) {
//            ToastUtils.makeToast(e.message)
            Log.d("Error : ", "rotateImage: Image not found.")
        }
        val orientation = ei!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotatePicture(
                galleryBitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotatePicture(
                galleryBitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotatePicture(
                galleryBitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> galleryBitmap
            else -> galleryBitmap
        }
    }

    fun rotatePicture(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}