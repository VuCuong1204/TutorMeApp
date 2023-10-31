package vn.tutorme.mobile.presenter.dialog

import androidx.core.widget.addTextChangedListener
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.InputFeedbackDialogBinding

class InputFeedBackDialog : TutorMeDialog<InputFeedbackDialogBinding>(R.layout.input_feedback_dialog) {

    var listener: IInputFeedBackListener? = null

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = true
    }

    override fun getBackgroundId(): Int = R.id.flInputFeedBackRoot

    override fun onInitView() {
        super.onInitView()

        binding.tvInputFeedBackClose.setOnSafeClick { dismiss() }

        binding.tvInputFeedBackConfirm.setOnSafeClick {
            val text = binding.edtInputFeedBackContent.text.toString().trim()
            if (text.isNotEmpty()) {
                dismiss()
                listener?.onConfirmClick(text)
            }
        }

        binding.edtInputFeedBackContent.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                binding.tvInputFeedBackConfirm.apply {
                    isEnabled = false
                    background = getAppDrawable(R.drawable.shape_bg_gray_corner_16)
                }
            } else {
                binding.tvInputFeedBackConfirm.apply {
                    isEnabled = true
                    background = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
                }
            }
        }
    }

    interface IInputFeedBackListener {
        fun onConfirmClick(text: String)
    }
}
