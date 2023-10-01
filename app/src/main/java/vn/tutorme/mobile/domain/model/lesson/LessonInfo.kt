package vn.tutorme.mobile.domain.model.lesson

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class LessonInfo(
    var lessonId: String? = null,
    var classId: String? = null,
    var timeBegin: Long? = null,
    var timeEnd: Long? = null,
    var status: LESSON_STATUS? = null,
    var title: String? = null,
    var level: String? = null,
    var memberNumber: Int? = null,
    var totalNumber: Int? = null,
    var lessonSession: Int? = null,
    var assessment: Int? = null
) : IParcelable {
    fun getAssessmentState(): Boolean {
        return assessment != null
    }

    fun getTimeLearn(): String {
        return "19:00 - 19:45"
    }

    fun getNumberMember(): String {
        return String.format(getAppString(R.string.number_member), totalNumber)
    }

    fun getNumberMemberRatio(): String {
        return String.format(getAppString(R.string.number_member_lesson), memberNumber, totalNumber)
    }

    fun getNumberEvaluate(): String {
        return String.format(getAppString(R.string.number_evaluate_lesson), assessment, totalNumber)
    }

    fun getSession(): String {
        return String.format(getAppString(R.string.lesson_session), lessonSession)
    }

    fun getTimeBegin(): String {
        return "12.12.2023"
    }
}

fun mockDataLessonInfo(size: Int = 9): List<LessonInfo> {

    val list = mutableListOf<LessonInfo>()

    repeat(size) {
        list.add(LessonInfo(
            classId = "Mã lớp D5C.045137",
            status = LESSON_STATUS.UPCOMING_STATUS,
            title = "Lớp 2",
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
            status = LESSON_STATUS.UPCOMING_STATUS,
            title = "Lớp 2",
            level = "Nâng cao",
            memberNumber = 20,
            totalNumber = 20,
            lessonSession = 12,
            assessment = 10
        ))
    }

    return list
}
