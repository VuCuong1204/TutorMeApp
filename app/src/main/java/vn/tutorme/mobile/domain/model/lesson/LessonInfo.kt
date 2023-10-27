package vn.tutorme.mobile.domain.model.lesson

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.utils.TimeUtils

@Parcelize
data class LessonInfo(
    var lessonId: Int? = null,
    var classId: String? = null,
    var timeBegin: Long? = null,
    var timeEnd: Long? = null,
    var status: LESSON_STATUS? = null,
    var nameClass: String? = null,
    var level: String? = null,
    var memberNumber: Int? = null,
    var totalNumber: Int? = null,
    var lessonSession: Int? = null,
    var type: LESSON_TYPE? = null,
    var countAssessment: Int? = null,
    var nameTeacher: String? = null,
    var emailTeacher: String? = null,
    var phoneNumberTeacher: Long? = null,
) : IParcelable {
    fun getAssessmentState(): Boolean {
        return type == LESSON_TYPE.NOT_YET_RATE_TYPE
    }

    fun getTimeLearnHour(): String {
        return "${getHourBegin()} - ${getHourEnd()}"
    }

    fun getTimeLearnDay(): String {
        return "${getDayBegin()} - ${getDayEnd()}"
    }

    fun getNumberMember(): String {
        return String.format(getAppString(R.string.number_member), totalNumber)
    }

    fun getNumberMemberRatio(): String {
        return String.format(getAppString(R.string.number_member_lesson), memberNumber, totalNumber)
    }

    fun getNumberEvaluate(): String {
        return String.format(getAppString(R.string.number_evaluate_lesson), countAssessment, memberNumber)
    }

    fun getSession(): String {
        return String.format(getAppString(R.string.lesson_session), lessonSession)
    }

    fun getTimeBegin(): String {
        return "12.12.2023"
    }

    fun getClassTitle(): String {
        return nameClass ?: Extension.STRING_DEFAULT
    }

    fun getHourBegin(): String {
        return TimeUtils.convertTimeToHour(timeBegin ?: Extension.LONG_DEFAULT)
    }

    fun getHourEnd(): String {
        return TimeUtils.convertTimeToHour(timeEnd ?: Extension.LONG_DEFAULT)
    }

    fun getDayBegin(): String {
        return TimeUtils.convertTimeToDay(timeBegin ?: Extension.LONG_DEFAULT)
    }

    fun getDayEnd(): String {
        return TimeUtils.convertTimeToDay(timeEnd ?: Extension.LONG_DEFAULT)
    }
}

fun mockDataLessonInfo(size: Int = 9): List<LessonInfo> {

    val list = mutableListOf<LessonInfo>()

    repeat(size) {
        list.add(LessonInfo(
            classId = "Mã lớp D5C.045137",
            status = LESSON_STATUS.UPCOMING_STATUS,
            nameClass = "Lớp 3",
            level = "Nâng cao",
            memberNumber = 20,
            totalNumber = 20,
            lessonSession = 12
        ))
    }

    return list
}

fun mockDataLessonInfoEvaluate(size: Int = 9): List<LessonInfo> {
    val list = mutableListOf<LessonInfo>()

    repeat(size) {
        list.add(LessonInfo(
            classId = "Mã lớp D5C.045137",
            status = LESSON_STATUS.HAPPENING_STATUS,
            nameClass = "Lớp 3",
            level = "Nâng cao",
            memberNumber = 20,
            totalNumber = 20,
            lessonSession = 20,
            countAssessment = 10
        ))
    }

    return list
}
