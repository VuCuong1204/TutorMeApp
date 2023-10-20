package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<DeleteNotificationUseCase.DeleteNotificationRV, Boolean>() {

    override suspend fun execute(rv: DeleteNotificationRV): Boolean {
        return notificationRepo.deleteNotification(rv.notificationId)
    }

    class DeleteNotificationRV(val notificationId: Int) : RequestValue
}
