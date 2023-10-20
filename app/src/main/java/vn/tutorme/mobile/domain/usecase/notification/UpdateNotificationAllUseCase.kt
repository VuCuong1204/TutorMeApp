package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class UpdateNotificationAllUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<UpdateNotificationAllUseCase.UpdateNotificationAllRV, Boolean>() {

    override suspend fun execute(rv: UpdateNotificationAllRV): Boolean {
        return notificationRepo.updateNotificationAll(rv.userId)
    }

    class UpdateNotificationAllRV(val userId: String) : RequestValue
}
