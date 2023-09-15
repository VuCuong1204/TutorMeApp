package vn.tutorme.mobile.base.common.view

import android.view.View
import java.lang.System.currentTimeMillis

const val DELAY_BETWEEN_TIME_DEFAULT = 350L

abstract class SingleOnClickListener(
    private val delayBetweenTime: Long = DELAY_BETWEEN_TIME_DEFAULT
) : View.OnClickListener {

    var timeCurrent = -1L

    abstract fun onDebouncedClick(view: View)

    override fun onClick(v: View) {
        val now = currentTimeMillis()
        if (now >= timeCurrent + delayBetweenTime) {
            onDebouncedClick(v)
        }

        timeCurrent = now
    }
}
