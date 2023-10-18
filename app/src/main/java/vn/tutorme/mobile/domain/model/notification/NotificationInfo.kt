package vn.tutorme.mobile.domain.model.notification

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.utils.TimeUtils

@Parcelize
data class NotificationInfo(
    var id: String? = null,
    var title: String? = null,
    var content: String? = null,
    var notifyState: NOTIFICATION_STATE? = null,
    var notifyType: NOTIFICATION_TYPE? = null,
    var timeSend: Long? = null,
    var userId: String? = null,
    var isLastIndex: Boolean? = null
) : IParcelable {

    fun getTime(): String {
        return TimeUtils.calculatorTimeAgo(timeSend)
    }
}

fun mockNotificationList(size: Int = 20): List<NotificationInfo> {

    val typeList = listOf(NOTIFICATION_TYPE.SYSTEM_TYPE, NOTIFICATION_TYPE.APPROVE_TYPE, NOTIFICATION_TYPE.PREPARE_STUDY_TYPE)
    val stateList = listOf(NOTIFICATION_STATE.READ_STATE, NOTIFICATION_STATE.UNREAD_STATE)
    val list = mutableListOf<NotificationInfo>()
    repeat(size) {
        list.add(NotificationInfo(
            id = "$it",
            title = "Lớp học sẽ diễn ra trong 20 phút!",
            content = "Con chuẩn bị có một lớp học vào lúc 18h, bố mẹ nhắc nhở con bật mic, bật lap để tránh vấn đề về...",
            notifyState = stateList.random(),
            notifyType = typeList.random(),
            timeSend = 1697605800,
            userId = "vucuonghihi"
        ))
    }

    return list
}
