package vn.tutorme.mobile.base.screen

import android.graphics.drawable.Drawable
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.base.BaseBindingDialog
import vn.tutorme.mobile.base.common.screenstate.IDisplayState
import vn.tutorme.mobile.presenter.main.MainActivity
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TIME_SHOWN
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TYPE

abstract class TutorMeDialog<DB : ViewDataBinding>(layoutId: Int) : BaseBindingDialog<DB>(layoutId), IDisplayState {

    private val mainActivity by lazy {
        requireActivity() as MainActivity
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
}
