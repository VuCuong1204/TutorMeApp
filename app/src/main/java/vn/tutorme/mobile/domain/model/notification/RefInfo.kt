package vn.tutorme.mobile.domain.model.notification

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class RefInfo(
    var lessonId: Int? = null,
    var classId: String? = null
) : IParcelable
