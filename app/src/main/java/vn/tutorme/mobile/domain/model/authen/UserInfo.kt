package vn.tutorme.mobile.domain.model.authen

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class UserInfo(

    var userId: String? = null,

    var fullName: String? = null,

    var date: String? = null,

    var address: String? = null,

    var nameSchool: String? = null,

    var gender: GENDER_TYPE? = null,

    var phoneNumber: Long? = null,

    var role: ROLE_TYPE? = null,

    var avatar: String? = null

) : IParcelable {
    fun getGenderUser(): String {
        return when (gender) {
            GENDER_TYPE.MALE_TYPE -> getAppString(R.string.male)
            GENDER_TYPE.FEMALE_TYPE -> getAppString(R.string.female)
            GENDER_TYPE.OTHER -> getAppString(R.string.other)
            else -> getAppString(R.string.other)
        }
    }
}
