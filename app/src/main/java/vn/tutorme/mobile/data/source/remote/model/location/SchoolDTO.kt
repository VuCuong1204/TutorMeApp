package vn.tutorme.mobile.data.source.remote.model.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SchoolDTO(
    @SerializedName("schoolId")
    @Expose
    var schoolId: String? = null,

    @SerializedName("provincesCityId")
    @Expose
    var provincesCityId: String? = null,

    @SerializedName("districtId")
    @Expose
    var districtId: String? = null,

    @SerializedName("schoolName")
    @Expose
    var schoolName: String? = null,

    @SerializedName("level")
    @Expose
    var level: String? = null
)
