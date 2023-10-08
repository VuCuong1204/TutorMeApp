package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

interface ILessonRepo {
    fun getMissionInfo(id: String, timeBegin: Long, timeEnd: Long): List<LessonInfo>
    fun getLessonInfoTeacher(id: String, timeBegin: Long, timeEnd: Long, stateRate: Int?, page: Int?, size: Int?): List<LessonInfo>
    fun getClassInfoRegisterTeach(currentTime: Long?, state: Int): List<ClassInfo>
    fun getLessonStudentInDay(studentId: String, beginTime: Long, endTime: Long, page: Int?, size: Int?): List<LessonInfo>
    fun getClassStudentRegistered(studentId: String, page: Int?, size: Int?): List<ClassInfo>

    fun getCourseList(currentTime: Long, page: Int?, size: Int?): List<CourseInfo>
    fun getLessonListInClass(classId: String, page: Int?, size: Int?): List<LessonInfo>
}
