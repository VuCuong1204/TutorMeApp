package vn.tutorme.mobile.data.source.remote.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseApiResponse : IApiResponse {

    @SerializedName("msg")
    @Expose
    var msg: String? = null

    @SerializedName("code")
    @Expose
    var code: Int? = null

    override fun isSuccessful(): Boolean {
        return msg != null && code == 0
    }
}
