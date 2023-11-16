package vn.tutorme.mobile.presenter.lessonevaluate

import vn.tutorme.mobile.domain.model.lesson.LessonInfo

interface ILessonEvaluateListener {
    fun onItemClick(item: LessonInfo)
}
