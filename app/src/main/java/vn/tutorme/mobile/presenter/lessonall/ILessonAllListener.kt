package vn.tutorme.mobile.presenter.lessonall

import vn.tutorme.mobile.domain.model.lesson.LessonInfo

interface ILessonAllListener {
    fun onItemClick(item: LessonInfo)
}
