package vn.tutorme.mobile.data.source.remote.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class NotificationInfoResponse(
    @SerializedName("data")
    @Expose
    var data: List<NotificationInfoDTO>? = null

) : BaseApiResponse()
