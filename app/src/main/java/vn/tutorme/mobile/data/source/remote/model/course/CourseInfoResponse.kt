package vn.tutorme.mobile.data.source.remote.model.course

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class CourseInfoResponse(
    @SerializedName("data")
    @Expose
    var data: List<CourseInfoDTO>? = null

) : BaseApiResponse()
