package vn.tutorme.mobile.data.source.remote.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoMainDTO(
    @SerializedName("userInfo")
    @Expose
    var userInfo: UserInfoDTO? = null
)
