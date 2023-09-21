package vn.tutorme.mobile.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class UserInfoResponse(
    @SerializedName("data")
    @Expose
    var data: UserInfoDTO? = null

) : BaseApiResponse()
