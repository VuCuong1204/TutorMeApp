package vn.tutorme.mobile.presenter.authen.login

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.CountNotifyEvent
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.SLIDE_TYPE
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.common.sociallogin.FacebookLogin
import vn.tutorme.mobile.base.common.sociallogin.GoogleLogin
import vn.tutorme.mobile.base.common.sociallogin.ISocialTokenListener
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.isEmailValid
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LoginFragmentBinding
import vn.tutorme.mobile.presenter.authen.register.RegisterFragment
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.widget.textfield.INPUT_TYPE

@AndroidEntryPoint
class LoginFragment : TutorMeFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    private val viewModel by viewModels<LoginViewModel>()

    private var facebookLogin: FacebookLogin? = null
    private var googleLogin: GoogleLogin? = null

    private lateinit var auth: FirebaseAuth

    override fun onInitView() {
        super.onInitView()

        auth = Firebase.auth
        addHeader()
        initSocialLogin()
        addEventOnClick()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.userInfoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        clearBackStackFragment()
                        replaceFragment(
                            fragment = HomeFragment(),
                            screenAnim = SlideAnimation()
                        )
                        EventBusManager.instance?.postPending(CountNotifyEvent())
                        mainActivity.setBottomBarType()
                        viewModel.resetState()
                    }
                }, canShowLoading = true)
            }
        }
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

        AppPreferences.userNameAccount?.let { binding.tfvLoginAccount.setTextContent(it) }
        if (AppPreferences.checkSaveInfo == true) {
            binding.ivLoginCheck.setImageDrawable(getAppDrawable(R.drawable.ic_tick_show))
            AppPreferences.passwordAccount?.let { binding.tfvLoginPassword.setTextContent(it) }
        } else binding.ivLoginCheck.setImageDrawable(getAppDrawable(R.drawable.ic_tick_gone))
    }

    private fun addEventOnClick() {

        binding.ivLoginCheck.setOnSafeClick {
            binding.ivLoginCheck.setImageDrawable(
                if (!AppPreferences.checkSaveInfo!!) getAppDrawable(R.drawable.ic_tick_show)
                else getAppDrawable(R.drawable.ic_tick_gone)
            )

            AppPreferences.checkSaveInfo = !AppPreferences.checkSaveInfo!!
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
            googleLogin?.login()
        }

        binding.ivLoginFacebook.setOnSafeClick {
            facebookLogin?.login()
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
                val id = auth.currentUser?.uid
                id?.let {
                    AppPreferences.userNameAccount = email
                    AppPreferences.passwordAccount = password
                    viewModel.login(it)
                }
            } else {
                showLoginFailedDialog()
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
                val id = auth.currentUser?.uid
                id?.let { viewModel.register(it) }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "${exception.message}")
                hideLoading()
            }
    }

    private fun showLoginFailedDialog() {
        BottomSheetConfirmDialog().apply {
            eventLeftClick {}
        }.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }
}
