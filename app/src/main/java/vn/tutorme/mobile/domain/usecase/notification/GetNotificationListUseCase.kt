package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.LIMIT_SIZE
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class GetNotificationListUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<GetNotificationListUseCase.GetNotificationListRV, List<NotificationInfo>>() {

    override suspend fun execute(rv: GetNotificationListRV): List<NotificationInfo> {
        return notificationRepo.getNotificationList(rv.userId, rv.page, rv.size)
    }

    class GetNotificationListRV(val userId: String) : RequestValue {
        var page: Int? = INT_DEFAULT
        var size: Int? = LIMIT_SIZE
    }
}
