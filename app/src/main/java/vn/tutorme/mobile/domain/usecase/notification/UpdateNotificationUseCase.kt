package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<UpdateNotificationUseCase.UpdateNotificationRV, Boolean>() {

    override suspend fun execute(rv: UpdateNotificationRV): Boolean {
        return notificationRepo.updateNotification(rv.notificationId)
    }

    class UpdateNotificationRV(val notificationId: Int) : RequestValue
}
