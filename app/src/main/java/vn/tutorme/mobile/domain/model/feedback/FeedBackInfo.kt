package vn.tutorme.mobile.domain.model.feedback

data class FeedBackInfo(
    var id: Int? = null,
    var rating: Int? = null,
    var comment: String? = null,
    var lessonId: Int? = null
)
