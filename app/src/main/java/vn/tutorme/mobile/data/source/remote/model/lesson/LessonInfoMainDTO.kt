package vn.tutorme.mobile.data.source.remote.model.lesson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.model.classinfo.ClassInfoDTO
import vn.tutorme.mobile.data.source.remote.model.user.UserInfoDTO

data class LessonInfoMainDTO(
    @SerializedName("lessonInfo")
    @Expose
    var lessonInfo: LessonInfoDTO? = null,

    @SerializedName("classInfo")
    @Expose
    var classInfo: ClassInfoDTO? = null,

    @SerializedName("userInfo")
    @Expose
    var userInfo: UserInfoDTO? = null,
)
