package vn.tutorme.mobile.base.common.screenstate

import android.graphics.drawable.Drawable
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TIME_SHOWN
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TYPE

interface IDisplayState {
    fun showLoading()
    fun hideLoading()

    fun showCustomToast(
        msg: CharSequence?,
        type: HEADER_ALERT_TYPE = HEADER_ALERT_TYPE.CUSTOM,
        timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_DEFAULT,
        icon: Drawable? = null,
        bgColor: Int? = null
    )

    fun showMessage(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_DEFAULT)
    fun showSuccess(msg: CharSequence?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_DEFAULT)
    fun showError(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_DEFAULT)
    fun showWarning(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN = HEADER_ALERT_TIME_SHOWN.DELAY_DEFAULT)
}
