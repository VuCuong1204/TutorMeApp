package vn.tutorme.mobile.presention.widget.headeralert

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.basegraduate.presentation.widget.headeralert.MessageInfo
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.screen.TutorMeActivity

open class HeaderAlertDefault(activity: TutorMeActivity<*>) : HeaderAlert(activity) {

    var timeShown = HEADER_ALERT_TIME_SHOWN.DELAY_2_SECOND
    private val messageEnqueue = arrayListOf<MessageInfo>()
    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable {
        dismiss()
    }

    override fun onCreateView(activity: TutorMeActivity<*>, type: HEADER_ALERT_TYPE): ViewGroup {
        return LayoutInflater.from(activity).inflate(R.layout.layout_header_alert, null) as ViewGroup
    }

    override fun onViewCreated(view: ViewGroup, type: HEADER_ALERT_TYPE) {
        val ivBackground = view.findViewById<RelativeLayout>(R.id.rlHeaderAlertBg)
        val ivIcon = view.findViewById<ImageView>(R.id.ivHeaderAlertIcon)
        val tvTitle = view.findViewById<TextView>(R.id.tvHeaderAlert)

        when (type) {
            HEADER_ALERT_TYPE.SUCCESS -> {
                ivIcon.setImageDrawable(getAppDrawable(R.drawable.ic_success_white))
                ivBackground.setBackgroundColor(getAppColor(R.color.success))
            }

            HEADER_ALERT_TYPE.WARNING -> {
                ivIcon.setImageDrawable(getAppDrawable(R.drawable.ic_info))
                ivBackground.setBackgroundColor(getAppColor(R.color.warning))
            }

            HEADER_ALERT_TYPE.ERROR -> {
                ivIcon.setImageDrawable(getAppDrawable(R.drawable.ic_info))
                ivBackground.setBackgroundColor(getAppColor(R.color.error))
            }

            HEADER_ALERT_TYPE.CUSTOM -> {
                ivIcon.setImageDrawable(icon)
                ivBackground.setBackgroundColor(getAppColor(bgColor ?: R.color.success))
            }
        }
        tvTitle.text = message

        handler.postDelayed(dismissRunnable, timeShown.time)
    }

    override fun enqueueMessage(msg: CharSequence?, type: HEADER_ALERT_TYPE) {
        super.enqueueMessage(msg, type)
        var exist = false
        messageEnqueue.forEach {
            if (it.isSame(msg)) {
                exist = true
                return@forEach
            }
        }
        if (!exist) {
            val msgInfo = MessageInfo(msg = msg, type = type)
            messageEnqueue.add(msgInfo)
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        Log.e("HeaderAlert", "onDismiss: ${messageEnqueue.size}")
        if (messageEnqueue.isNotEmpty()) {
            val nextMsg = messageEnqueue.removeAt(0)
            show(nextMsg.msg, nextMsg.type!!)
        }
    }

    override fun destroy() {
        handler.removeCallbacks(dismissRunnable)
        messageEnqueue.clear()
    }
}
