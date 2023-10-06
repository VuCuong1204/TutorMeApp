package vn.tutorme.mobile.domain.model.clazz

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.utils.TimeUtils

@Parcelize
data class ClassInfo(
    var classId: String? = null,
    var timeBeginLearn: String? = null,
    var level: String? = null,
    var classStatus: CLASS_STATUS? = null,
    var titleClass: String? = null,
    var totalNumber: Int? = null,
    var timeBegin: Long? = null,
    var teacherId: String? = null
) : IParcelable {
    fun getTimeDayBegin(): String {
        val time = TimeUtils.convertTimeToDay(timeBegin ?: LONG_DEFAULT)
        return String.format(getAppString(R.string.opening), time)
    }

    fun getNumberMember(): String {
        return String.format(getAppString(R.string.number_member), totalNumber)
    }
}

fun mockDataClassInfo(size: Int = 6): List<ClassInfo> {

    val list = mutableListOf<ClassInfo>()

    repeat(size) {
        list.add(ClassInfo(
            classId = "Mã lớp D5C.045137",
            level = "Nâng cao",
            totalNumber = 20,
            classStatus = CLASS_STATUS.EMPTY_CLASS_STATUS,
            titleClass = "Lớp 2"
        ))
    }

    return list
}
