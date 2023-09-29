package vn.tutorme.mobile.data.source.remote.model.lesson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.model.classinfo.ClassInfoDTO

data class LessonInfoMainDTO(
    @SerializedName("lessonInfo")
    @Expose
    var lessonInfo: LessonInfoDTO? = null,

    @SerializedName("classInfo")
    @Expose
    var classInfo: ClassInfoDTO? = null,
)
