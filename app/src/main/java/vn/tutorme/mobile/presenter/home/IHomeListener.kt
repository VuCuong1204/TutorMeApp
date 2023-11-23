package vn.tutorme.mobile.presenter.home

import vn.tutorme.mobile.domain.model.banner.Banner
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

interface IHomeListener {
    fun onItemBannerClick(item: Banner)
    fun onClickTeachViewMore()
    fun onClickEvaluateViewMore()
    fun onClickClassRegisterViewMore()
    fun onClickClassWaitingConfirm()
    fun onClickConfirmRegisterClass(classId: String)
    fun onClickCourseInfo(courseId: String)
    fun onClickClassInfo(classId: String)
    fun onClickLessonInfo(item: LessonInfo)
    fun onClickViewMoreCourse()
}
