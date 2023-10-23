package vn.tutorme.mobile.data.source.remote.model.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SchoolResponse {
    @SerializedName("data")
    @Expose
    var data: List<SchoolDTO>? = null
}
