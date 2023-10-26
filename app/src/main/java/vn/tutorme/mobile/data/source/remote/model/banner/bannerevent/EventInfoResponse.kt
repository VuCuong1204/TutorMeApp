package vn.tutorme.mobile.data.source.remote.model.banner.bannerevent

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class EventInfoResponse(
    @SerializedName("data")
    @Expose
    var data: BannerInfoEventDTO? = null
) : BaseApiResponse()
