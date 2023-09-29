package vn.tutorme.mobile.data.source.remote.model.banner.bannerjob

import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class BannerInfoJobResponse(
    var data: List<BannerInfoJobDTO>? = null
) : BaseApiResponse()
