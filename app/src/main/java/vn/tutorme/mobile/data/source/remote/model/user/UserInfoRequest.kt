package vn.tutorme.mobile.data.source.remote.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoRequest(
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

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: Long? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null
)
