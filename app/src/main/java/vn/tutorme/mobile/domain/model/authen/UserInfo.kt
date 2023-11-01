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

    var avatar: String? = null,

    var attendanceState: ATTENDANCE_STATE? = null,

    var evaluateState: EVALUATE_STATE? = null

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

fun mockDataUserInfo(size: Int = 20): List<UserInfo> {
    val list = mutableListOf<UserInfo>()
    val genderList = listOf(GENDER_TYPE.MALE_TYPE, GENDER_TYPE.FEMALE_TYPE, GENDER_TYPE.OTHER)
    val attendanceStateList = listOf(ATTENDANCE_STATE.ROLL_CALLED_STATE, ATTENDANCE_STATE.NO_ROLL_CALLED_STATE)
    val evaluateStateList = listOf(EVALUATE_STATE.HAVE_EVALUATE_STATE, EVALUATE_STATE.NO_EVALUATE_STATE)
    repeat(size) {
        list.add(UserInfo(
            userId = "$it",
            fullName = "Vũ Cường",
            date = "12/04/2001",
            gender = genderList.random(),
            attendanceState = attendanceStateList.random(),
            evaluateState = evaluateStateList.random()
        ))
    }

    return list
}
