package vn.tutorme.mobile.data.source.remote.model.classinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.model.course.CourseInfoDTO

data class ClassInfoMainDTO(
    @SerializedName("classInfo")
    @Expose
    var classInfo: ClassInfoDTO? = null,

    @SerializedName("courseInfo")
    @Expose
    var courseInfo: CourseInfoDTO? = null
)
