package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.notification.NotificationInfoDTO
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.model.notification.RefInfo

class NotificationInfoDTOConvertToNotificationInfo : IConverter<List<NotificationInfoDTO>, List<NotificationInfo>> {
    override fun convert(source: List<NotificationInfoDTO>): List<NotificationInfo> {
        val list = mutableListOf<NotificationInfo>()
        source.forEach {
            list.add(NotificationInfo(
                id = it.id,
                title = it.title,
                content = it.content,
                notifyState = NOTIFICATION_STATE.valueOfName(it.notifyState),
                notifyType = NOTIFICATION_TYPE.valueOfName(it.notifyType),
                timeSend = it.timeSend,
                refInfo = RefInfo(
                    it.refInfo?.lessonId,
                    it.refInfo?.classId
                )
            ))
        }

        return list
    }
}
