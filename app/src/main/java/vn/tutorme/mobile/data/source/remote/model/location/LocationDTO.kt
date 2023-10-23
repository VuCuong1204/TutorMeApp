package vn.tutorme.mobile.data.source.remote.model.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationDTO(
    @SerializedName("locationId")
    @Expose
    var locationId: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null

)
