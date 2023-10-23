package vn.tutorme.mobile.data.source.remote.model.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationResponse {
    @SerializedName("data")
    @Expose
    var data: List<LocationDTO>? = null
}
