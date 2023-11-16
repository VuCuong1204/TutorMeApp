package vn.tutorme.mobile.data.source.remote.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class ReviewDetailStudentResponse(
    @SerializedName("data")
    @Expose
    var data: UserInfoDTO? = null
) : BaseApiResponse()
