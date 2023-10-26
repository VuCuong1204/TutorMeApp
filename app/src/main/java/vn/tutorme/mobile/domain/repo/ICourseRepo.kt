package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.banner.BannerEventInfo
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo

interface ICourseRepo {
    fun getCourseInfo(courseId: String): CourseInfo
    fun getClassListFromCourse(courseId: String): List<ClassInfo>
    fun checkCourseRegistered(courseId: String, userId: String): Boolean
    fun registerCourse(classId: String, studentId: String, lessonFirst: Int, lessonSecond: Int): Boolean
    fun getBannerInfoEvent(eventId: Int): BannerEventInfo
}
