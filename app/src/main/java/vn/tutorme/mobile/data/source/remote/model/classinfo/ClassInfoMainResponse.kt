package vn.tutorme.mobile.data.source.remote.model.classinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class ClassInfoMainResponse(
    @SerializedName("data")
    @Expose
    var data: List<ClassInfoDTO>? = null

) : BaseApiResponse()
