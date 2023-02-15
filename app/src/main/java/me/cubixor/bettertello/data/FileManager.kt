package me.cubixor.bettertello.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import me.cubixor.bettertello.App.Companion.instance
import me.cubixor.telloapi.api.listeners.FileReceiver
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileManager : FileReceiver {

    override fun onPhotoReceived(data: ByteArray) {
        val localDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val fileName = localDateTime.format(formatter) + ".jpg"
        val file = File(createDir(), fileName)

        try {
            FileOutputStream(file).use { out ->
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                val exif = ExifInterface(file.canonicalPath)
                exif.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, LocalDateTime.now().toString())
                exif.saveAttributes()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        MediaScannerConnection.scanFile(
            instance.applicationContext, arrayOf(file.absolutePath), null, null
        )
    }


    fun createDir(): File {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() +
                    File.separator + "BetterTello"
        )
        dir.mkdirs()
        return dir
    }
}