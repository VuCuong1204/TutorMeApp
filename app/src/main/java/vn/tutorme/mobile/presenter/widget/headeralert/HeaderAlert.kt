package vn.tutorme.mobile.presenter.widget.headeralert

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import com.example.basegraduate.presentation.widget.headeralert.IHeaderAlert
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.screen.TutorMeActivity

abstract class HeaderAlert(private val activity: TutorMeActivity<*>) : IHeaderAlert {
    var message: CharSequence? = null
    var icon: Drawable? = null
    var bgColor: Int? = null
    var alertView: ViewGroup? = null
    private var isShowing = false

    override fun show(msg: CharSequence?, type: HEADER_ALERT_TYPE) {
        if (isShowing) {
            if (msg != message) {
                enqueueMessage(msg, type)
            }
            return
        }

        isShowing = true
        message = msg
        if (alertView == null) {
            alertView = onCreateView(activity, type)
            addView(alertView!!)
        }

        onViewCreated(alertView!!, type)
        showAnim(alertView!!, type)
    }

    override fun dismiss() {
        message = null
        dismissAnim(alertView!!)
    }

    abstract fun onCreateView(activity: TutorMeActivity<*>, type: HEADER_ALERT_TYPE): ViewGroup
    abstract fun onViewCreated(view: ViewGroup, type: HEADER_ALERT_TYPE)

    open fun addView(view: ViewGroup) {
        activity.window.addContentView(
            alertView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    open fun showAnim(view: ViewGroup, type: HEADER_ALERT_TYPE) {
        val animDown = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom)
        val animController = LayoutAnimationController(animDown)
        view.visibility = View.VISIBLE
        view.layoutAnimation = animController
        view.startAnimation(animDown)
    }

    open fun dismissAnim(view: ViewGroup) {
        val animUp = AnimationUtils.loadAnimation(activity, R.anim.slide_out_top)
        val animController = LayoutAnimationController(animUp)
        view.visibility = View.GONE
        view.layoutAnimation = animController
        animUp.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                isShowing = false
                onDismiss()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        view.startAnimation(animUp)
    }

    open fun onDismiss() {}
    open fun enqueueMessage(msg: CharSequence?, type: HEADER_ALERT_TYPE) {}
}
