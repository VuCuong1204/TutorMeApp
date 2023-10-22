package vn.tutorme.mobile.presenter.profile.changepassword

import android.view.inputmethod.EditorInfo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ChangePasswordFragmentBinding
import vn.tutorme.mobile.presenter.widget.textfield.INPUT_TYPE

@AndroidEntryPoint
class ChangePasswordFragment : TutorMeFragment<ChangePasswordFragmentBinding>(R.layout.change_password_fragment) {

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onInitView() {
        super.onInitView()
        addHeader()
    }

    private fun addHeader() {
        binding.tfvChangePasswordOld.apply {
            setInputType(INPUT_TYPE.TEXT_PASSWORD_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_NEXT)
        }

        binding.tfvChangePasswordNew.apply {
            setInputType(INPUT_TYPE.TEXT_PASSWORD_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_NEXT)
        }

        binding.tfvChangePasswordNewConfirm.apply {
            setInputType(INPUT_TYPE.TEXT_PASSWORD_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_DONE)
        }

        binding.tvChangePasswordConfirm.setOnSafeClick {
            checkChange()
        }

        binding.ivInformationPersonBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }

    private fun checkChange() {
        val passwordOld = binding.tfvChangePasswordOld.getTextContent()
        val passwordNew = binding.tfvChangePasswordNew.getTextContent()
        val passwordConfirm = binding.tfvChangePasswordNewConfirm.getTextContent()

        when {
            passwordOld.isEmpty() -> {
                binding.tfvChangePasswordOld.showError(getAppString(R.string.field_has_not_filled))
            }

            (passwordOld.length < 6 || passwordOld.length > 20) -> {
                binding.tfvChangePasswordOld.showError(getAppString(R.string.password_wrong_limit))
            }

            passwordNew.isEmpty() -> {
                binding.tfvChangePasswordNew.showError(getAppString(R.string.field_has_not_filled))
            }

            (passwordNew.length < 6 || passwordNew.length > 20) -> {
                binding.tfvChangePasswordNew.showError(getAppString(R.string.password_wrong_limit))
            }

            passwordConfirm.isEmpty() -> {
                binding.tfvChangePasswordNewConfirm.showError(getAppString(R.string.field_has_not_filled))
            }

            (passwordConfirm.length < 6 || passwordConfirm.length > 20) -> {
                binding.tfvChangePasswordNewConfirm.showError(getAppString(R.string.password_confirm_wrong_limit))
            }

            passwordNew != passwordConfirm -> {
                binding.tfvChangePasswordNewConfirm.showError(getAppString(R.string.password_wrong_not_equal))
            }

            else -> {
                changePassword(passwordOld, passwordNew, passwordConfirm)
            }
        }
    }

    private fun changePassword(passwordOld: String, passwordNew: String, passwordConfirm: String) {
        when {
            AppPreferences.passwordAccount == STRING_DEFAULT -> {
                showError(getAppString(R.string.change_password_3rd))
            }

            passwordOld != AppPreferences.passwordAccount -> {
                showError(getAppString(R.string.password_old_error))
            }

            passwordNew != passwordConfirm -> {
                showError(getAppString(R.string.password_wrong_not_equal))
            }

            else -> {
                showLoading()
                if (AppPreferences.userNameAccount != "") {
                    user?.updatePassword(passwordConfirm)
                        ?.addOnCompleteListener(mainActivity) { task ->
                            if (task.isSuccessful) {
                                showSuccess(getAppString(R.string.change_password_success))
                                binding.tfvChangePasswordOld.setTextContent("")
                                binding.tfvChangePasswordNew.setTextContent("")
                                binding.tfvChangePasswordNewConfirm.setTextContent("")
                                AppPreferences.passwordAccount = passwordConfirm
                                hideLoading()
                            } else {
                                hideLoading()
                                showError(getAppString(R.string.change_password_error))
                            }
                        }
                } else {
                    hideLoading()
                    showError(getAppString(R.string.change_password_error))
                }
            }
        }
    }
}
