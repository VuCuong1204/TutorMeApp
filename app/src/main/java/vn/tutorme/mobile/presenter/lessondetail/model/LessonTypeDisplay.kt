package vn.tutorme.mobile.presenter.lessondetail.model

import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

data class LessonTypeDisplay(
    var lessonInfo: LessonInfo? = null,
    var userInfo: UserInfo? = null,
    var type: LESSON_STATUS? = null
)
