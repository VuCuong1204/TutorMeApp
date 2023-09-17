package vn.tutorme.mobile.base.common.sociallogin

import android.app.Activity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import vn.tutorme.mobile.R

class FacebookLogin(activity: Activity, val tokenListener: ISocialTokenListener) : SocialLogin(tokenListener) {

    private val callbackManager = CallbackManager.Factory.create()
    private val loginListener = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            tokenListener.onSuccess(result.accessToken.token)
        }

        override fun onCancel() {
            tokenListener.onCancel()
        }

        override fun onError(error: FacebookException) {
            tokenListener.onFailed(error)
        }
    }

    private val loginContract: LoginManager.FacebookLoginActivityResultContract
    private var loginLauncher: ActivityResultLauncher<Collection<String>>? = null

    init {
        registerCallbackLoginFacebook(activity)
        loginContract = LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    }

    override fun register(caller: ActivityResultCaller) {
        loginLauncher = caller.registerForActivityResult(loginContract) {}
    }

    override fun login() {
        loginLauncher?.launch(listOf("public_profile"))
    }

    override fun release(activity: Activity) {
        unregisterCallbackLoginFacebook()
        loginLauncher = null
    }

    override fun logout() {
        LoginManager.getInstance().logOut()
    }

    private fun registerCallbackLoginFacebook(activity: Activity) {
        val facebookAppId = activity.getString(R.string.facebook_app_id)
        val facebookClientId = activity.getString(R.string.facebook_client_token)
        if (facebookAppId.isNotEmpty() && facebookClientId.isNotEmpty()) {
            FacebookSdk.setApplicationId(facebookAppId)
            FacebookSdk.setClientToken(facebookClientId)
        }

        FacebookSdk.sdkInitialize(activity)
        LoginManager.getInstance().registerCallback(callbackManager, loginListener)
    }

    private fun unregisterCallbackLoginFacebook() {
        LoginManager.getInstance().unregisterCallback(callbackManager)
    }
}
