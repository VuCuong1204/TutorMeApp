package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class InsertNotificationUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<InsertNotificationUseCase.InsertNotificationRV, Boolean>() {
    override suspend fun execute(rv: InsertNotificationRV): Boolean {
        return notificationRepo.insertNotification(
            rv.title,
            rv.content,
            rv.notifyState.value,
            rv.notifyType.value,
            rv.timeSend,
            rv.userId,
            rv.lessonId,
            rv.classId
        )
    }

    class InsertNotificationRV(val userId: String) : RequestValue {
        var title: String = STRING_DEFAULT
        var content: String = STRING_DEFAULT
        var notifyState: NOTIFICATION_STATE = NOTIFICATION_STATE.READ_STATE
        var notifyType: NOTIFICATION_TYPE = NOTIFICATION_TYPE.PREPARE_STUDY_TYPE
        var timeSend: Long = LONG_DEFAULT
        var lessonId: Int = INT_DEFAULT
        var classId: String = STRING_DEFAULT
    }
}
