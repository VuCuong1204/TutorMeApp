package vn.tutorme.mobile.data.source.remote.model.lesson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class LessonInfoResponse(
    @SerializedName("data")
    @Expose
    var data: List<LessonInfoMainDTO>? = null
) : BaseApiResponse()
