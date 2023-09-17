package vn.tutorme.mobile.base.screen

import android.graphics.drawable.Drawable
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.base.BaseBindingActivity
import vn.tutorme.mobile.presenter.widget.headeralert.HeaderAlertDefault
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.base.common.eventbus.IEventHandler
import vn.tutorme.mobile.base.common.screenstate.IDisplayState
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TIME_SHOWN
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TYPE

abstract class TutorMeActivity<DB : ViewDataBinding>(layoutId: Int) : BaseBindingActivity<DB>(layoutId), IDisplayState, IEventHandler {
    private var headerAlertDefault: HeaderAlertDefault? = null

    override fun showCustomToast(
        msg: CharSequence?,
        type: HEADER_ALERT_TYPE,
        timeShown: HEADER_ALERT_TIME_SHOWN,
        icon: Drawable?,
        bgColor: Int?
    ) {

        if (headerAlertDefault == null) {
            headerAlertDefault = HeaderAlertDefault(this)
        }

        headerAlertDefault?.timeShown = timeShown
        if (type == HEADER_ALERT_TYPE.CUSTOM && icon != null && bgColor != null) {
            headerAlertDefault?.icon = icon
            headerAlertDefault?.bgColor = bgColor
        }
        headerAlertDefault?.show(msg, type)
    }

    override fun showMessage(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        showCustomToast(msg = msg, type = HEADER_ALERT_TYPE.CUSTOM, timeShown = timeShown)
    }

    override fun showSuccess(msg: CharSequence?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        showCustomToast(msg = msg, type = HEADER_ALERT_TYPE.SUCCESS, timeShown = timeShown)
    }

    override fun showError(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        showCustomToast(msg = msg, type = HEADER_ALERT_TYPE.ERROR, timeShown = timeShown)

    }

    override fun showWarning(msg: String?, timeShown: HEADER_ALERT_TIME_SHOWN) {
        showCustomToast(msg = msg, type = HEADER_ALERT_TYPE.CUSTOM, timeShown = timeShown)
    }

    override fun onEvent(event: IEvent) {
        // TODO("Not yet implemented")
    }
}
