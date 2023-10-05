package vn.tutorme.mobile.data.source.remote.model.lesson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LessonInfoDTO(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("extraClassId")
    @Expose
    var extraClassId: String? = null,

    @SerializedName("beginTime")
    @Expose
    var beginTime: Long? = null,

    @SerializedName("endTime")
    @Expose
    var endTime: Long? = null,

    @SerializedName("positionLesson")
    @Expose
    var positionLesson: Int? = null,

    @SerializedName("banner")
    @Expose
    var banner: String? = null,

    @SerializedName("state")
    @Expose
    var state: Int? = null,

    @SerializedName("typeLesson")
    @Expose
    var typeLesson: Int? = null,

    @SerializedName("rating")
    @Expose
    var rating: Float? = null,

    @SerializedName("updateAssessment")
    @Expose
    var updateAssessment: Int? = null,

    @SerializedName("assessmentCount")
    @Expose
    var assessmentCount: Int? = null,
)
