package vn.tutorme.mobile.domain.usecase.course

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo
import vn.tutorme.mobile.domain.repo.ICourseRepo
import javax.inject.Inject

class GetBannerInfoEventUseCase @Inject constructor(
    private val courseRepo: ICourseRepo
) : BaseUseCase<GetBannerInfoEventUseCase.GetBannerInfoEventRV, BannerEventInfo>() {
    override suspend fun execute(rv: GetBannerInfoEventRV): BannerEventInfo {
        return courseRepo.getBannerInfoEvent(rv.eventId)
    }

    class GetBannerInfoEventRV(val eventId: Int) : RequestValue
}
