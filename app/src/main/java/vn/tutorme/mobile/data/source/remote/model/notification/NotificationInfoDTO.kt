package vn.tutorme.mobile.data.source.remote.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationInfoDTO(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("notifyState")
    @Expose
    var notifyState: Int? = null,

    @SerializedName("notifyType")
    @Expose
    var notifyType: Int? = null,

    @SerializedName("timeSend")
    @Expose
    var timeSend: Long? = null,

    @SerializedName("userId")
    @Expose
    var userId: String? = null
)
