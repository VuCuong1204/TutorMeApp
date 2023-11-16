package vn.tutorme.mobile.data.source.remote.model.videocall

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoInfoResponse(
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
)
