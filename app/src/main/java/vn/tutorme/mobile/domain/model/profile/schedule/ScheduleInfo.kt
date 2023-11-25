package vn.tutorme.mobile.domain.model.profile.schedule

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.domain.model.profile.DAY_TYPE

data class ScheduleInfo(
    var timeBegin: Int? = null,
    var timeEnd: Int? = null,
    var rank: DAY_TYPE? = null,
    var className: String? = null,
    var classId: String? = null,
    var lessonCount: Int? = null,
    var content: String? = null
) {
    fun getLessonName(): String {
        return String.format(getAppString(R.string.lesson_session), lessonCount)
    }
}
