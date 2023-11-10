package vn.tutorme.mobile.presenter.dialog

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.ConfirmVideoDialogBinding

class ConfirmVideoDialog : TutorMeDialog<ConfirmVideoDialogBinding>(R.layout.confirm_video_dialog) {

    var listener: IConfirmVideoListener? = null
    var userName: String? = null

    override fun getBackgroundId() = R.id.flConfirmVideoRoot

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = false
    }

    override fun onInitView() {
        super.onInitView()

        userName?.let { binding.tvConfirmVideoUserName.text = it }
        binding.ivConfirmVideoAccept.setOnSafeClick {
            listener?.onConfirmClick()
            dismiss()
        }

        binding.ivConfirmVideoReject.setOnSafeClick {
            listener?.onRejectClick()
            dismiss()
        }
    }

    interface IConfirmVideoListener {
        fun onRejectClick()
        fun onConfirmClick()
    }
}
