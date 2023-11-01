package vn.tutorme.mobile.data.source.remote.model.lesson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class LessonInfoMainResponse(
    @SerializedName("data")
    @Expose
    var data: LessonInfoMainDTO? = null,
) : BaseApiResponse()
