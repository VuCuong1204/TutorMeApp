package vn.tutorme.mobile.base.common.activityresultlauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class OpenAppSettingResult : IActivityResult<Activity, Boolean>() {

    override fun getActivityContract(): ActivityResultContract<Activity, Boolean> {
        return OpenAppSettingContract()
    }

    private class OpenAppSettingContract : ActivityResultContract<Activity, Boolean>() {

        override fun createIntent(context: Context, input: Activity): Intent {
            val uri = Uri.fromParts("package", input.packageName, null)
            return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = uri
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return (resultCode == Activity.RESULT_OK) && (intent != null)
        }
    }
}
