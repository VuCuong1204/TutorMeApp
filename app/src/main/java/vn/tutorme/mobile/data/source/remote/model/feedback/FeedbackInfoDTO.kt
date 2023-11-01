package vn.tutorme.mobile.data.source.remote.model.feedback

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedbackInfoDTO(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("rating")
    @Expose
    var rating: Int? = null,

    @SerializedName("comment")
    @Expose
    var comment: String? = null,

    @SerializedName("lessonId")
    @Expose
    var lessonId: Int? = null
)
