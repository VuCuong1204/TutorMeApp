package vn.tutorme.mobile.data.source.remote.model.banner.bannerevent

import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class BannerInfoEventResponse(
    var data: List<BannerInfoEventDTO>? = null
) : BaseApiResponse()
