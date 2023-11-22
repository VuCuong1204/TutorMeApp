package vn.tutorme.mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {

    fun saveBitmap(context: Context, bitmap: Bitmap): String? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        try {
            val imageFile = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )

            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()

            return imageFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getFile(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp_file")
        file.createNewFile()

        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.let {
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
        }

        return file
    }
}
