package vn.tutorme.mobile.presenter.login

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.sociallogin.FacebookLogin
import vn.tutorme.mobile.base.common.sociallogin.GoogleLogin
import vn.tutorme.mobile.base.common.sociallogin.ISocialTokenListener
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LoginFragmentBinding

class LoginFragment : TutorMeFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    private var facebookLogin: FacebookLogin? = null
    private var googleLogin: GoogleLogin? = null

    override fun onInitView() {
        super.onInitView()
        initSocialLogin()
        addEventOnClick()
    }

    override fun onDestroyView() {
        facebookLogin?.release(mainActivity)
        facebookLogin = null
        googleLogin?.release(mainActivity)
        googleLogin = null
        super.onDestroyView()
    }

    private fun initSocialLogin() {
        facebookLogin = FacebookLogin(mainActivity, object : ISocialTokenListener {
            override fun onSuccess(token: String, code: String?) {
                val credential = FacebookAuthProvider.getCredential(token)
                singInFirebase(credential)
            }

            override fun onFailed(e: Exception?) {
                Log.d(TAG, "${e?.message}")
                hideLoading()
            }

            override fun onCancel() {
                hideLoading()
            }
        }).also {
            it.register(this)
        }

        googleLogin = GoogleLogin(mainActivity, object : ISocialTokenListener {
            override fun onSuccess(token: String, code: String?) {
                val credential = GoogleAuthProvider.getCredential(token, null)
                singInFirebase(credential)
            }

            override fun onCancel() {
                hideLoading()
            }

            override fun onFailed(e: Exception?) {
                hideLoading()
                Log.d(TAG, "${e?.message}")
            }
        }).also {
            it.register(this)
        }
    }

    private fun singInFirebase(credential: AuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnSuccessListener { _ ->
                val token = Firebase.auth.currentUser?.getIdToken(false)?.result?.token
                Log.d(TAG, "singInFirebase: $token")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "${exception.message}")
                hideLoading()
            }
    }

    private fun addEventOnClick() {
        binding.btnMainGoogle.setOnSafeClick {
            facebookLogin?.login()
        }

        binding.btnMainFacebook.setOnSafeClick {
            facebookLogin?.logout()
        }
    }

    override fun showLoading() {
        //   TODO("Not yet implemented")
    }

    override fun hideLoading() {
        //   TODO("Not yet implemented")
    }
}
