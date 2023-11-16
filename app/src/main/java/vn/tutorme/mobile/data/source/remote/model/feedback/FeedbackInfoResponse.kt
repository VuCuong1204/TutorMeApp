package vn.tutorme.mobile.data.source.remote.model.feedback

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class FeedbackInfoResponse(
    @SerializedName("data")
    @Expose
    var data: List<FeedbackInfoDTO>? = null
) : BaseApiResponse()
