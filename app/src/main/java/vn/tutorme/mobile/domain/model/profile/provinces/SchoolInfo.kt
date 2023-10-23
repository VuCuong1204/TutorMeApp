package vn.tutorme.mobile.domain.model.profile.provinces

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
class SchoolInfo(

    var schoolId: String? = null,

    var provincesCityId: String? = null,

    var districtId: String? = null,

    var schoolName: String? = null,

    var level: String? = null

) : IParcelable
