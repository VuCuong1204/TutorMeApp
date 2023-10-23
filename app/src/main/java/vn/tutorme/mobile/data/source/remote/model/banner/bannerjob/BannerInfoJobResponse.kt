package vn.tutorme.mobile.data.source.remote.model.banner.bannerjob

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class BannerInfoJobResponse(
    @SerializedName("data")
    @Expose
    var data: List<BannerInfoJobDTO>? = null
) : BaseApiResponse()
