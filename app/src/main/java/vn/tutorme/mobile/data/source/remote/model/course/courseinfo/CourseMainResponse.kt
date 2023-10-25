package vn.tutorme.mobile.data.source.remote.model.course.courseinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse
import vn.tutorme.mobile.data.source.remote.model.course.CourseInfoDTO

data class CourseMainResponse(
    @SerializedName("data")
    @Expose
    var data: CourseInfoDTO? = null

) : BaseApiResponse()
