package vn.tutorme.mobile.base.screen

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Window
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.base.BaseBindingDialog
import vn.tutorme.mobile.base.common.screenstate.IDisplayState
import vn.tutorme.mobile.presenter.main.MainActivity
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TIME_SHOWN
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TYPE
import vn.tutorme.mobile.presenter.widget.headeralert.HeaderAlertDialogDefault

abstract class TutorMeDialog<DB : ViewDataBinding>(layoutId: Int) : BaseBindingDialog<DB>(layoutId), IDisplayState {

    private var headerAlert: HeaderAlertDialogDefault? = null

    private var window: Window? = null

    val mainActivity by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        window = dialog.window
        return dialog
    }

    override fun showLoading() {
        mainActivity.showLoading()
    }

    override fun hideLoading() {
        mainActivity.hideLoading()
    }

    override fun showCustomToast(msg: CharSequence?, type: HEADER_ALERT_TYPE, timeShown: HEADER_ALERT_TIME_SHOWN, icon: Drawable?, bgColor: Int?) {
        mainActivity.showCustomToast(msg, type, timeShown, icon, bgColor)
    }

    override fun showMessage(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        mainActivity.showMessage(msg, timeShown)
    }

    override fun showSuccess(msg: CharSequence?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        mainActivity.showSuccess(msg, timeShown)
    }

    override fun showError(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        mainActivity.showError(msg, timeShown)
    }

    override fun showWarning(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        mainActivity.showWarning(msg, timeShown)
    }

    fun showSuccessDialog(msg: CharSequence?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_2_SECOND) {
        showCustomToastDialog(
            msg = msg,
            type = HEADER_ALERT_TYPE.SUCCESS,
            timeShown = timeShown
        )
    }

    fun showErrorDialog(msg: CharSequence?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_2_SECOND) {
        showCustomToastDialog(
            msg = msg,
            type = HEADER_ALERT_TYPE.ERROR,
            timeShown = timeShown
        )
    }

    fun showWarningDialog(msg: CharSequence?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_2_SECOND) {
        showCustomToastDialog(
            msg = msg,
            type = HEADER_ALERT_TYPE.WARNING,
            timeShown = timeShown
        )
    }

    private fun showCustomToastDialog(
        msg: CharSequence?,
        type: HEADER_ALERT_TYPE,
        timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_2_SECOND,
        icon: Drawable? = null,
        bgColor: Int? = null
    ) {
        if (headerAlert == null) {
            if (window != null) {
                headerAlert = HeaderAlertDialogDefault(this, window!!)
            }
        }
        headerAlert?.timeShown = timeShown
        if (type == HEADER_ALERT_TYPE.CUSTOM && icon != null && bgColor != null) {
            headerAlert?.icon = icon
            headerAlert?.bgColor = bgColor
        }
        headerAlert?.show(msg, type)
    }
}
