package vn.tutorme.mobile.base.common.view

import android.view.MotionEvent
import android.view.View
import java.lang.System.currentTimeMillis

abstract class SingleOnTouchClickListener(
    private val delayTime: Long = DELAY_BETWEEN_TIME_DEFAULT
) : View.OnTouchListener {
    private var timeCurrent = -1L
    private var downState: Boolean = false

    abstract fun onDebouncedClick(view: View)
    abstract fun onChangeText(view: View)

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                val now = currentTimeMillis()
                if (now >= timeCurrent + delayTime) {
                    if (v != null) {
                        onChangeText(v)
                        downState = true
                    }
                }
                timeCurrent = now
            }

            MotionEvent.ACTION_UP -> {
                if (v != null && downState) {
                    onDebouncedClick(v)
                    downState = false
                }
            }
        }

        return true
    }
}
