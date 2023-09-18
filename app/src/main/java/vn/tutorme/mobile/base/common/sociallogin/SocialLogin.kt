package vn.tutorme.mobile.base.common.sociallogin

import android.app.Activity
import androidx.activity.result.ActivityResultCaller

abstract class SocialLogin(val callback: ISocialTokenListener) {
    abstract fun register(caller: ActivityResultCaller)
    abstract fun login()
    abstract fun release(activity: Activity)
    abstract fun logout()
}

interface ISocialTokenListener {
    fun onSuccess(token: String, code: String? = null)
    fun onCancel()
    fun onFailed(e: Exception?)
}
