package vn.tutorme.mobile.data.source.remote.model.banner.bannerevent

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BannerInfoEventDTO(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("banner")
    @Expose
    var banner: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("createTime")
    @Expose
    var createTime: Long? = null,

    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("countRegister")
    @Expose
    var countRegister: Int? = null,

    @SerializedName("describe")
    @Expose
    var describe: String? = null,

    @SerializedName("joinInstructions")
    @Expose
    var joinInstructions: String? = null
)
