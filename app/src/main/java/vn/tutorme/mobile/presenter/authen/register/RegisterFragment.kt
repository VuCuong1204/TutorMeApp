package vn.tutorme.mobile.presenter.authen.register

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.FadeAnim
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.isEmailValid
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.RegisterFragmentBinding
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.widget.textfield.INPUT_TYPE

@AndroidEntryPoint
class RegisterFragment : TutorMeFragment<RegisterFragmentBinding>(R.layout.register_fragment) {

    private val viewModel by viewModels<RegisterViewModel>()

    private lateinit var auth: FirebaseAuth

    override fun onInitView() {
        super.onInitView()

        auth = Firebase.auth
        addHeader()

        binding.btnRegister.setOnSafeClick {
            binding.tfvRegisterUsername.clearFocus()
            binding.tfvRegisterPassword.clearFocus()
            binding.tfvRegisterPasswordConfirm.clearFocus()
            checkLogin()
        }

        binding.ivRegisterClose.setOnSafeClick {
            backFragment()
        }
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
                            screenAnim = FadeAnim()
                        )
                    }
                }, canShowLoading = true)
            }
        }
    }

    private fun addHeader() {
        binding.tfvRegisterUsername.apply {
            setInputType(INPUT_TYPE.TEXT_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_NEXT)
        }

        binding.tfvRegisterPassword.apply {
            setInputType(INPUT_TYPE.TEXT_PASSWORD_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_NEXT)
        }

        binding.tfvRegisterPasswordConfirm.apply {
            setInputType(INPUT_TYPE.TEXT_PASSWORD_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_DONE)
        }
    }

    private fun checkLogin() {
        val username = binding.tfvRegisterUsername.getTextContent()
        val password = binding.tfvRegisterPassword.getTextContent()
        val passwordConfirm = binding.tfvRegisterPasswordConfirm.getTextContent()

        when {
            username.isEmpty() -> {
                binding.tfvRegisterUsername.showError(getAppString(R.string.field_has_not_filled))
            }

            !isEmailValid(username) -> {
                binding.tfvRegisterUsername.showError(getAppString(R.string.account_wrong))
            }

            password.isEmpty() -> {
                binding.tfvRegisterPassword.showError(getAppString(R.string.field_has_not_filled))
            }

            (password.length < 6 || password.length > 20) -> {
                binding.tfvRegisterPassword.showError(getAppString(R.string.password_wrong_limit))
            }

            passwordConfirm.isEmpty() -> {
                binding.tfvRegisterPasswordConfirm.showError(getAppString(R.string.field_has_not_filled))
            }

            (passwordConfirm.length < 6 || passwordConfirm.length > 20) -> {
                binding.tfvRegisterPassword.showError(getAppString(R.string.password_wrong_limit))
            }

            passwordConfirm != password -> {
                binding.tfvRegisterPasswordConfirm.showError(getAppString(R.string.password_wrong_not_equal))
            }

            else -> {
                registerAccount(username, password)
            }
        }
    }

    private fun registerAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(mainActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val id = auth.currentUser?.uid
                    id?.let {
                        AppPreferences.token = auth.currentUser?.getIdToken(false)?.result?.token
                        viewModel.register(it, email, password)
                    }
                } else {
                    Log.d(TAG, "createUserWithEmail:failure", task.exception)
                    showError(getAppString(R.string.register_fail))
                }
            }
    }
}
