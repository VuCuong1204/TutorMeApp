package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.domain.model.banner.BANNER_TYPE
import vn.tutorme.mobile.domain.model.banner.Banner
import vn.tutorme.mobile.domain.repo.IBannerRepo
import javax.inject.Inject

class GetBannerInfoUseCase @Inject constructor(
    private val bannerRepo: IBannerRepo
) : BaseUseCase<GetBannerInfoUseCase.GetBannerInfoRV, List<Banner>>() {
    override suspend fun execute(rv: GetBannerInfoRV): List<Banner> {
        val bannerEvent = bannerRepo.getBannerEvent(rv.page, rv.size)
        val bannerJob = bannerRepo.getBannerJob(rv.page, rv.size)

        val bannerList = mutableListOf<Banner>()

        bannerJob.forEachIndexed { index, bannerJobInfo ->
            if (index == 3) return@forEachIndexed
            bannerList.add(Banner(
                id = bannerJobInfo.id,
                link = bannerJobInfo.banner,
                type = BANNER_TYPE.JOB_TYPE,
            ))
        }

        bannerEvent.forEachIndexed { index, bannerEventInfo ->
            if (index == 2) return@forEachIndexed
            bannerList.add(Banner(
                id = bannerEventInfo.id,
                link = bannerEventInfo.banner,
                type = BANNER_TYPE.EVENT_TYPE,
            ))
        }

        return bannerList
    }

    class GetBannerInfoRV : RequestValue {
        var page: Int? = INT_DEFAULT
        var size: Int? = 5
    }
}
