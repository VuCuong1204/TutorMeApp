package vn.tutorme.mobile.presenter.authen.forgotpassword

import android.content.Intent
import android.net.Uri
import android.view.inputmethod.EditorInfo
import com.google.firebase.auth.FirebaseAuth
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.isEmailValid
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ForgotPasswordFragmentBinding
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.widget.textfield.INPUT_TYPE

class ForgotPasswordFragment : TutorMeFragment<ForgotPasswordFragmentBinding>(R.layout.forgot_password_fragment) {

    override fun onInitView() {
        super.onInitView()

        binding.tfvForgotPasswordAccount.apply {
            setInputType(INPUT_TYPE.TEXT_TYPE)
            setImgOptions(EditorInfo.IME_ACTION_NEXT)
        }

        binding.ivForgotPasswordBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.tvForgotPasswordConfirm.setOnSafeClick { checkEmail() }
    }

    private fun checkEmail() {
        val username = binding.tfvForgotPasswordAccount.getTextContent()
        when {
            username.isEmpty() -> {
                binding.tfvForgotPasswordAccount.showError(getAppString(R.string.field_has_not_filled))
            }

            !isEmailValid(username) -> {
                binding.tfvForgotPasswordAccount.showError(getAppString(R.string.account_wrong))
            }

            else -> {
                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(username)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showCheckEmailDialog()
                        } else {
                            showError(getAppString(R.string.email_not_exist))
                        }
                    }
            }
        }
    }

    private fun showCheckEmailDialog() {
        BottomSheetConfirmDialog().apply {
            title = getAppString(R.string.send_request_forgot_password)
            content = getAppString(R.string.please_coming_email)
            textLeft = getAppString(R.string.confirm)
            textRight = getAppString(R.string.close)
            eventLeftClick {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(getAppString(R.string.email_to))
                emailIntent.putExtra(Intent.EXTRA_TEXT, "inbox")
                mainActivity.startActivity(emailIntent)
            }
        }.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }
}
