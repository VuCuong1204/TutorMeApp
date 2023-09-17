package vn.tutorme.mobile.base.common.sociallogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString

class GoogleLogin(activity: Activity, private val tokenListener: ISocialTokenListener) : SocialLogin(tokenListener) {
    companion object {
        private val TAG = GoogleLogin::class.java.simpleName
    }

    private var googleSignInClient: GoogleSignInClient
    private val logInContract: ActivityResultContract<Unit, GoogleSignInAccount?>
    private var logInLauncher: ActivityResultLauncher<Unit>? = null

    init {
        val clientId = getAppString(R.string.web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)

        logInContract = object : ActivityResultContract<Unit, GoogleSignInAccount?>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return googleSignInClient.signInIntent
            }

            override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
                var account: GoogleSignInAccount? = null
                if (intent != null) {
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                        account = task.getResult(ApiException::class.java)
                    } catch (e: ApiException) {
                        Log.i("parseResult", "parseResult: ${e.message}")
                        if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                            tokenListener.onCancel()
                        } else {
                            tokenListener.onFailed(e)
                        }
                        e.printStackTrace()
                    }
                }
                return account
            }
        }
    }

    override fun register(caller: ActivityResultCaller) {
        logInLauncher = caller.registerForActivityResult(logInContract) { account ->
            account?.let {
                Log.i(TAG, "idToken Google: ${account.idToken}")
                tokenListener.onSuccess(account.idToken!!)
            }
        }
    }

    override fun login() {
        googleSignInClient.revokeAccess()
            .addOnCompleteListener {
                logInLauncher?.launch(Unit)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    override fun release(activity: Activity) {
        logInLauncher = null
    }

    override fun logout() {
        googleSignInClient.signOut()
    }
}
