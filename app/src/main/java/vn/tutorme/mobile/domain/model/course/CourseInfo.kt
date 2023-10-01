package vn.tutorme.mobile.domain.model.course

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class CourseInfo(
    var courseId: String? = null,
    var banner: String? = null,
    var title: String? = null,
    var ratingTotal: Float? = null,
    var ratingNumber: Int? = null,
    var memberRegister: Int? = null,
    var dateEnd: Long? = null,
    var price: Long? = null
) : IParcelable
