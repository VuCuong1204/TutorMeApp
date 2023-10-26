package vn.tutorme.mobile.data.repo.banner

import vn.tutorme.mobile.data.repo.convert.BannerEventListDTOConvertToBannerListEvent
import vn.tutorme.mobile.data.repo.convert.BannerJobDTOConvertToBannerJob
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.service.ILessonService
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo
import vn.tutorme.mobile.domain.model.banner.BannerJobInfo
import vn.tutorme.mobile.domain.repo.IBannerRepo
import javax.inject.Inject

class BannerRepoImpl @Inject constructor() : IBannerRepo, IRepo {
    override fun getBannerEvent(page: Int?, size: Int?): List<BannerEventInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getBannerEvent(page, size).invokeApi { _, body ->
            BannerEventListDTOConvertToBannerListEvent().convert(body.data!!)
        }
    }

    override fun getBannerJob(page: Int?, size: Int?): List<BannerJobInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getBannerJob(page, size).invokeApi { _, body ->
            BannerJobDTOConvertToBannerJob().convert(body.data!!)
        }
    }
}
