package vn.tutorme.mobile.presenter.dialog

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.ConfirmDialogBinding

class ConfirmDialog : TutorMeDialog<ConfirmDialogBinding>(R.layout.confirm_dialog) {

    private var onLeftClick: (() -> Unit)? = null
    private var onRightClick: (() -> Unit)? = null

    override fun getBackgroundId(): Int = R.id.flConfirmRoot

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = true
    }

    override fun onInitView() {
        super.onInitView()

        binding.tvConfirmLeft.setOnSafeClick {
            dismiss()
            onLeftClick?.invoke()
        }

        binding.tvConfirmRight.setOnSafeClick {
            dismiss()
            onRightClick?.invoke()
        }
    }

    fun eventLeftClick(action: () -> Unit) {
        onLeftClick = action
    }

    fun eventRightClick(action: () -> Unit) {
        onRightClick = action
    }
}
