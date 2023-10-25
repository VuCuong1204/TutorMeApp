package vn.tutorme.mobile.data.source.remote.model.classinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClassInfoDTO(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("nameClass")
    @Expose
    var nameClass: String? = null,

    @SerializedName("typeClass")
    @Expose
    var typeClass: Int? = null,

    @SerializedName("describeClass")
    @Expose
    var describeClass: String? = null,

    @SerializedName("courseId")
    @Expose
    var courseId: String? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null,

    @SerializedName("countStudent")
    @Expose
    var countStudent: Int? = null,

    @SerializedName("countLesson")
    @Expose
    var countLesson: Int? = null,

    @SerializedName("timeBegin")
    @Expose
    var timeBegin: Long? = null,

    @SerializedName("teacherId")
    @Expose
    var teacherId: String? = null,

    @SerializedName("lessonFirst")
    @Expose
    var lessonFirst: Int? = null,

    @SerializedName("lessonSecond")
    @Expose
    var lessonSecond: Int? = null
)
