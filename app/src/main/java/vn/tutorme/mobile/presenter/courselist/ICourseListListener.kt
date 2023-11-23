package vn.tutorme.mobile.presenter.courselist

import vn.tutorme.mobile.domain.model.course.CourseInfo

interface ICourseListListener {
    fun onItemClick(item: CourseInfo)
}
