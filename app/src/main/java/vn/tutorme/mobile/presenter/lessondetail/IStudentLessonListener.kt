package vn.tutorme.mobile.presenter.lessondetail

import vn.tutorme.mobile.domain.model.authen.UserInfo

interface IStudentLessonListener {
    fun onRateClick(item: UserInfo)
    fun onAttendanceClick()
    fun onJoinRoom()
}
