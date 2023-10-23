package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.banner.Banner
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo
import vn.tutorme.mobile.domain.model.banner.BannerJobInfo

interface IBannerRepo {
    fun getBannerEvent(page: Int?, size: Int?): List<BannerEventInfo>
    fun getBannerJob(page: Int?, size: Int?): List<BannerJobInfo>
}
