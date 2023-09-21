package vn.tutorme.mobile.presenter.authen.login

import android.util.Log
import android.view.inputmethod.EditorInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.anim.SLIDE_TYPE
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.common.sociallogin.FacebookLogin
import vn.tutorme.mobile.base.common.sociallogin.GoogleLogin
import vn.tutorme.mobile.base.common.sociallogin.ISocialTokenListener
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.isEmailValid
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LoginFragmentBinding
import vn.tutorme.mobile.presenter.authen.register.RegisterFragment
import vn.tutorme.mobile.presenter.widget.textfield.INPUT_TYPE

class LoginFragment : TutorMeFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    private var facebookLogin: FacebookLogin? = null
    private var googleLogin: GoogleLogin? = null

    private lateinit var auth: FirebaseAuth

    private var isCheckSavePassWord = false
    override fun onInitView() {
        super.onInitView()

        auth = Firebase.auth
        addHeader()
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

    private fun addHeader() {
        binding.tfvLoginAccount.apply {
            setInputType(INPUT_TYPE.TEXT_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_NEXT)
        }

        binding.tfvLoginPassword.apply {
            setInputType(INPUT_TYPE.TEXT_PASSWORD_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_DONE)
        }
    }

    private fun addEventOnClick() {

        binding.ivLoginCheck.setOnSafeClick {
            binding.ivLoginCheck.setImageDrawable(
                if (!isCheckSavePassWord) getAppDrawable(R.drawable.ic_tick_show)
                else getAppDrawable(R.drawable.ic_tick_gone)
            )

            isCheckSavePassWord = !isCheckSavePassWord
        }

        binding.tvLoginConfirm.setOnSafeClick {
            binding.tfvLoginAccount.clearFocus()
            binding.tfvLoginPassword.clearFocus()
            checkLogin()
        }

        binding.tvLoginRegister.setOnSafeClick {
            addFragment(
                RegisterFragment(),
                null,
                true,
                SlideAnimation(SLIDE_TYPE.BOTTOM_TO_TOP)
            )
        }

        binding.ivLoginGoogle.setOnSafeClick {
            Log.d("TAG", "singInEmailPassword: ${auth.currentUser?.uid}")
        }

        binding.ivLoginFacebook.setOnSafeClick {
            FirebaseAuth.getInstance().signOut()
            Log.d("TAG", "singInEmailPassword: ${auth.currentUser?.uid}")
        }
    }

    private fun checkLogin() {
        val username = binding.tfvLoginAccount.getTextContent()
        val password = binding.tfvLoginPassword.getTextContent()
        if (username.isEmpty()) {
            binding.tfvLoginAccount.showError(getAppString(R.string.field_has_not_filled))
        } else {
            if (!isEmailValid(username)) {
                binding.tfvLoginAccount.showError(getAppString(R.string.account_wrong))
            } else {
                if (password.isEmpty()) {
                    binding.tfvLoginPassword.showError(getAppString(R.string.field_has_not_filled))
                } else {
                    singInEmailPassword(username, password)
                }
            }
        }
    }

    private fun singInEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mainActivity) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("TAG", "singInEmailPassword: ${user?.email.toString()} ${user?.getIdToken(false)?.result?.token} ${user?.uid}")
                toast("success")
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.exception)
            }
        }
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
                Log.d(TAG, "${e?.message}")
                hideLoading()
            }
        }).also {
            it.register(this)
        }
    }

    private fun singInFirebase(credential: AuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnSuccessListener { _ ->
                val token = auth.currentUser?.getIdToken(false)?.result?.token
                Log.d(TAG, "singInFirebase: $token")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "${exception.message}")
                hideLoading()
            }
    }
}
