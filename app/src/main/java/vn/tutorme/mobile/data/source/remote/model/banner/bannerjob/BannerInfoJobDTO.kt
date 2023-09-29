package vn.tutorme.mobile.data.source.remote.model.banner.bannerjob

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BannerInfoJobDTO(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("banner")
    @Expose
    var banner: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("income")
    @Expose
    var income: String? = null,

    @SerializedName("countMember")
    @Expose
    var countMember: Int? = null,

    @SerializedName("deadlineRegister")
    @Expose
    var deadlineRegister: Long? = null,

    @SerializedName("describe")
    @Expose
    var describe: String? = null,

    @SerializedName("requestJob")
    @Expose
    var requestJob: String? = null,

    @SerializedName("benefit")
    @Expose
    var benefit: String? = null
)
