package vn.tutorme.mobile.data.source.remote.model.course

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CourseInfoDTO(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("banner")
    @Expose
    var banner: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("rating")
    @Expose
    var rating: Float? = null,

    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("subject")
    @Expose
    var subject: String? = null,

    @SerializedName("classCode")
    @Expose
    var extraClassId: Int? = null,

    @SerializedName("timeLesson")
    @Expose
    var timeLesson: Long? = null,

    @SerializedName("studentRegistered")
    @Expose
    var studentRegistered: Int? = null,

    @SerializedName("createDate")
    @Expose
    var createDate: Long? = null,

    @SerializedName("endDate")
    @Expose
    var endDate: Long? = null,

    @SerializedName("demoClass")
    @Expose
    var demoClass: Int? = null,

    @SerializedName("price")
    @Expose
    var price: Long? = null,
)
