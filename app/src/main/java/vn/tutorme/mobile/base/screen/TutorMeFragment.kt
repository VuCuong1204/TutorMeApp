package vn.tutorme.mobile.base.screen

import android.graphics.drawable.Drawable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.BaseBindingFragment
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.base.common.eventbus.IEventHandler
import vn.tutorme.mobile.base.common.screenstate.IDisplayState
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.hideToast
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.presenter.main.MainActivity
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TIME_SHOWN
import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TYPE

abstract class TutorMeFragment<DB : ViewDataBinding>(layoutId: Int) : BaseBindingFragment<DB>(layoutId), IDisplayState, IEventHandler {

    companion object {
        private var TIME_SPACE_DELAY = 2000L
    }

    override fun onInitView() {
        super.onInitView()
    }

    val mainActivity by lazy { requireActivity() as MainActivity }

    private var timeBeginClick: Long = 0L

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

    override fun onBackPressByFragment() {
        mainActivity.backFragment()
    }

    fun exitScreen() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - timeBeginClick > TIME_SPACE_DELAY) {
            toast(getAppString(R.string.main_exit_confirm))
        } else {
            mainActivity.finish()
            hideToast()
        }

        timeBeginClick = currentTime
    }

    fun replaceFragmentInitialState(fragmentInitial: Fragment, keepIndex: Int) {
        mainActivity.replaceFragmentInitialState(fragmentInitial, keepIndex)
    }

    fun replaceFragmentInitialState(fragmentInitial: Fragment) {
        mainActivity.replaceFragmentInitialState(fragmentInitial)
    }

    override fun onEvent(event: IEvent) {
//        TODO("Not yet implemented")
    }
}
