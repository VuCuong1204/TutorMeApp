package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.model.notification.mockNotificationList
import javax.inject.Inject

class GetNotificationInfoList @Inject constructor() : BaseUseCase<BaseUseCase.RequestValue, List<NotificationInfo>>() {
    override suspend fun execute(rv: RequestValue): List<NotificationInfo> {
        return mockNotificationList()
    }
}
