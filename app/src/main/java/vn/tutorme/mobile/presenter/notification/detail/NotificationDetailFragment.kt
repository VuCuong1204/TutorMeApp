package vn.tutorme.mobile.presenter.notification.detail

import android.os.Build
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.NotifyDetailResultEvent
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.NotificationDetailFragmentBinding
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.domain.model.notification.NotificationInfo

class NotificationDetailFragment : TutorMeFragment<NotificationDetailFragmentBinding>(R.layout.notification_detail_fragment) {

    companion object {
        const val OBJECT_NOTIFY_KEY = "OBJECT_NOTIFY_KEY"
    }

    private var notificationInfo: NotificationInfo? = null

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        notificationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(OBJECT_NOTIFY_KEY, NotificationInfo::class.java)
        } else {
            arguments?.getParcelable(OBJECT_NOTIFY_KEY) as? NotificationInfo
        }
    }

    override fun onInitView() {
        super.onInitView()
        addHeader()
    }

    private fun addHeader() {
        binding.ivNotificationDetailAvatar.setImageResource(when (notificationInfo?.notifyType) {
            NOTIFICATION_TYPE.APPROVE_TYPE -> R.drawable.ic_send
            NOTIFICATION_TYPE.PREPARE_STUDY_TYPE -> R.drawable.ic_clock
            NOTIFICATION_TYPE.SYSTEM_TYPE -> R.drawable.ic_trash
            else -> R.drawable.ic_trash
        })

        binding.tvNotificationDetailTime.text = notificationInfo?.getTime()
        binding.tvNotificationDetailTitle.text = notificationInfo?.title
        binding.tvNotificationDetailContent.text = notificationInfo?.content

        binding.ivNotificationDetailBack.setOnSafeClick {

            notificationInfo?.id?.let { id -> EventBusManager.instance?.postPending(NotifyDetailResultEvent(id)) }
            onBackPressByFragment()
        }
    }
}
