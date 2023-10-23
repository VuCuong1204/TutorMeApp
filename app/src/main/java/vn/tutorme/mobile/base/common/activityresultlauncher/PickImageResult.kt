package vn.tutorme.mobile.base.common.activityresultlauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class PickImageResult : IActivityResult<String?, Uri?>() {

    override fun getActivityContract(): ActivityResultContract<String?, Uri?> {
        return PickImageContract()
    }

    private class PickImageContract : ActivityResultContract<String?, Uri?>() {
        override fun createIntent(context: Context, input: String?): Intent {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }

            return pickIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            val data = intent?.data
            return if (resultCode == Activity.RESULT_OK && data != null) {
                data
            } else {
                null
            }
        }
    }
}
