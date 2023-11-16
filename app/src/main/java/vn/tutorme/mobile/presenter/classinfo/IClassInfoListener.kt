package vn.tutorme.mobile.presenter.classinfo

import vn.tutorme.mobile.domain.model.lesson.LessonInfo

interface IClassInfoListener {
    fun onItemClick(item: LessonInfo)
}
