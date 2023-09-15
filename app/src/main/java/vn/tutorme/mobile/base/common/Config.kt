package vn.tutorme.mobile.base.common

import androidx.annotation.ColorRes

const val LAYOUT_INVALID = -1

class StatusBar(@ColorRes var color: Int = android.R.color.transparent, var isDarkText: Boolean = true)

class DialogScreen(
    var mode: DIALOG_MODE = DIALOG_MODE.NORMAL,
    var isFullWidth: Boolean = false,
    var isFullHeight: Boolean = false,
    var isDismissByTouchOutSide: Boolean = true,
    var isDismissByOnBackPressed: Boolean = true
) {
    enum class DIALOG_MODE {
        SCALE,
        NORMAL,
        BOTTOM
    }
}
