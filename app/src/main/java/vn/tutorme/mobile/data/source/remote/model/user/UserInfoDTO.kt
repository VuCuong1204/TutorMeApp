package vn.tutorme.mobile.data.source.remote.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoDTO(
    @SerializedName("userId")
    @Expose
    var userId: String? = null,

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null,

    @SerializedName("address")
    @Expose
    var address: String? = null,

    @SerializedName("nameSchool")
    @Expose
    var nameSchool: String? = null,

    @SerializedName("gender")
    @Expose
    var gender: Int? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null,

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: Long? = null,

    @SerializedName("role")
    @Expose
    var role: Int? = null,

    @SerializedName("stateAttendance")
    @Expose
    var stateAttendance: Int? = null,

    @SerializedName("updateReview")
    @Expose
    var updateReview: Int? = null,

    @SerializedName("scoreAttitude")
    @Expose
    var scoreAttitude: Float? = null,

    @SerializedName("commentAttitude")
    @Expose
    var commentAttitude: String? = null,

    @SerializedName("scorePreparation")
    @Expose
    var scorePreparation: Float? = null,

    @SerializedName("commentPreparation")
    @Expose
    var commentPreparation: String? = null,

    @SerializedName("scoreAskQuestion")
    @Expose
    var scoreAskQuestion: Float? = null,

    @SerializedName("commentAskQuestion")
    @Expose
    var commentAskQuestion: String? = null,

    @SerializedName("scoreJoinTheDiscussion")
    @Expose
    var scoreJoinTheDiscussion: Float? = null,

    @SerializedName("commentJoinTheDiscussion")
    @Expose
    var commentJoinTheDiscussion: String? = null,

    @SerializedName("scoreAttention")
    @Expose
    var scoreAttention: Float? = null,

    @SerializedName("commentAttention")
    @Expose
    var commentAttention: String? = null,

    @SerializedName("scoreCompleteTheXercise")
    @Expose
    var scoreCompleteTheXercise: Float? = null,

    @SerializedName("commentCompleteTheXercise")
    @Expose
    var commentCompleteTheXercise: String? = null,

    @SerializedName("commentMedium")
    @Expose
    var commentMedium: String? = null
)
