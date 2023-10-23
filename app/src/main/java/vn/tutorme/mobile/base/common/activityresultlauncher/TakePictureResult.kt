package vn.tutorme.mobile.base.common.activityresultlauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import vn.tutorme.mobile.base.application.getApplication
import java.io.File

class TakePictureResult : IActivityResult<Unit?, Uri?>() {

    override fun getActivityContract(): ActivityResultContract<Unit?, Uri?> {
        return TakePicture()
    }

    private class TakePicture : ActivityResultContract<Unit?, Uri?>() {
        private val filename = getApplication().getExternalFilesDir(null)?.path + "/capture_image.jpg"
        private var imageUri: Uri? = null

        override fun createIntent(context: Context, input: Unit?): Intent {
            imageUri = FileProvider.getUriForFile(getApplication(), getApplication().packageName + ".provider", File(filename))
            return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT,
                    imageUri)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return if (resultCode == Activity.RESULT_OK && filename.isNotEmpty()) {
                imageUri
            } else {
                null
            }
        }
    }
}
