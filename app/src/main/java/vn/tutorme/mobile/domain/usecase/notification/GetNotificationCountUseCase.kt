package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class GetNotificationCountUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<GetNotificationCountUseCase.GetNotificationCountRV, Int>() {
    override suspend fun execute(rv: GetNotificationCountRV): Int {
        return notificationRepo.getCountNotificationUnRead(rv.userId)
    }

    class GetNotificationCountRV(val userId: String) : RequestValue
}
