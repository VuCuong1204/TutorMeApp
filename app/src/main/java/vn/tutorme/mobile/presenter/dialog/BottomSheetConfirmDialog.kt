package vn.tutorme.mobile.presenter.dialog

import android.graphics.drawable.Drawable
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.BottomSheetConfirmDialogBinding

class BottomSheetConfirmDialog : TutorMeDialog<BottomSheetConfirmDialogBinding>(R.layout.bottom_sheet_confirm_dialog) {

    var avatar: Drawable? = null
    var title: String? = null
    var content: String? = null
    var textLeft: String? = null
    var textRight: String? = null
    var bgTextLeft: Drawable? = null
    var bgTextRight: Drawable? = null
    var clTextLeft: Int? = null
    var clTextRight: Int? = null

    var onLeftClick: (() -> Unit)? = null
    var onRightClick: (() -> Unit)? = null

    override fun getBackgroundId(): Int = R.id.flBottomSheetConfirmRoot

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = false
    }

    override fun onInitView() {
        super.onInitView()

        avatar?.let { binding.civBottomSheetConfirmRoot.setAvatar(it) }
        title?.let { binding.tvBottomSheetConfirmTitle.text = it }
        content?.let { binding.tvBottomSheetConfirmContent.text = it }
        textLeft?.let { binding.tvBottomSheetConfirmLeft.text = it }
        textRight?.let { binding.tvBottomSheetConfirmRight.text = it }
        bgTextLeft?.let { binding.tvBottomSheetConfirmLeft.background = bgTextLeft }
        bgTextRight?.let { binding.tvBottomSheetConfirmRight.background = bgTextRight }
        clTextLeft?.let { binding.tvBottomSheetConfirmLeft.setTextColor(it) }
        clTextRight?.let { binding.tvBottomSheetConfirmRight.setTextColor(it) }

        binding.tvBottomSheetConfirmLeft.setOnSafeClick {
            dismiss()
            onLeftClick?.invoke()
        }

        binding.tvBottomSheetConfirmRight.setOnSafeClick {
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
