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
    var totalStudent: Int? = null,
    var totalLesson: Int? = null,
    var courseId: String? = null,
    var timeBegin: Long? = null,
    var teacherId: String? = null,
    var lessonFirst: Int? = null,
    var lessonSecond: Int? = null,
) : IParcelable {
    fun getTimeDayBegin(): String {
        val time = TimeUtils.convertTimeToDay(timeBegin ?: LONG_DEFAULT)
        return String.format(getAppString(R.string.opening), time)
    }

    fun getDayBegin(): String {
        return if (lessonSecond == 8) {
            String.format(getAppString(R.string.time_begin_sunday), lessonFirst)
        } else {
            String.format(getAppString(R.string.time_begin), lessonFirst, lessonSecond)
        }
    }

    fun getCountLesson(): String {
        return String.format(getAppString(R.string.count_lesson), totalLesson)
    }

    fun getNumberMember(): String {
        return String.format(getAppString(R.string.number_member), totalStudent)
    }
}

fun mockDataClassInfo(size: Int = 6): List<ClassInfo> {

    val list = mutableListOf<ClassInfo>()

    repeat(size) {
        list.add(ClassInfo(
            classId = "Mã lớp D5C.045137",
            level = "Nâng cao",
            totalStudent = 20,
            classStatus = CLASS_STATUS.EMPTY_CLASS_STATUS,
            titleClass = "Lớp 2"
        ))
    }

    return list
}
