package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import javax.inject.Inject

class GetNotificationCountUnread @Inject constructor() : BaseUseCase<BaseUseCase.RequestValue, Int>() {
    override suspend fun execute(rv: RequestValue): Int {
        return 30
    }
}
