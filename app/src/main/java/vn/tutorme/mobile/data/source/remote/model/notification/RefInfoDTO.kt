package vn.tutorme.mobile.data.source.remote.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefInfoDTO(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("lessonId")
    @Expose
    var lessonId: Int? = null,

    @SerializedName("classId")
    @Expose
    var classId: String? = null,
)
