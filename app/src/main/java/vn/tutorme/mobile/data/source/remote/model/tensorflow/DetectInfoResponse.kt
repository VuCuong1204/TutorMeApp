package vn.tutorme.mobile.data.source.remote.model.tensorflow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class DetectInfoResponse(
    @SerializedName("confidence")
    @Expose
    var confidence: Double? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null
) : BaseApiResponse()
