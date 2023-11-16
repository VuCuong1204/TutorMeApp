package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.model.feedback.FeedBackInfo
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

interface ILessonRepo {
    fun getMissionInfo(id: String, timeBegin: Long, timeEnd: Long): List<LessonInfo>
    fun getLessonInfoTeacher(id: String, timeBegin: Long, timeEnd: Long, stateRate: Int?, page: Int?, size: Int?): List<LessonInfo>
    fun getClassInfoRegisterTeach(currentTime: Long?, state: Int, teacherId: String?): List<ClassInfo>
    fun getLessonStudentInDay(studentId: String, beginTime: Long, endTime: Long, page: Int?, size: Int?): List<LessonInfo>
    fun getClassStudentRegistered(studentId: String, page: Int?, size: Int?): List<ClassInfo>

    fun getCourseList(currentTime: Long, page: Int?, size: Int?): List<CourseInfo>
    fun getLessonListInClass(classId: String, page: Int?, size: Int?): List<LessonInfo>
    fun updateStateClassRegister(classId: String, state: Int, teacherId: String): List<ClassInfo>
    fun getClassTeacherList(id: String, type: Int, page: Int?, size: Int?): List<ClassInfo>

    fun getLessonDetail(lessonId: Int): LessonInfo
    fun getStudentInLesson(classId: String): List<UserInfo>
    fun attendanceStudent(lessonId: Int, studentId: String): Boolean
    fun updateStateLesson(lessonId: Int, state: Int): LessonInfo
    fun feedBackLesson(lessonId: Int, content: String): Boolean
    fun getFeedbackList(lessonId: Int): List<FeedBackInfo>

    fun insertReviewDetail(
        scoreAttitude: Float,
        commentAttitude: String,
        scorePreparation: Float,
        commentPreparation: String,
        scoreAskQuestion: Float,
        commentAskQuestion: String,
        scoreJoinTheDiscussion: Float,
        commentJoinTheDiscussion: String,
        scoreAttention: Float,
        commentAttention: String,
        scoreCompleteTheXercise: Float,
        commentCompleteTheXercise: String,
        commentMedium: String,
        userId: String,
        lessonId: Int
    ): Boolean

    fun getReviewDetail(userId: String, lessonId: Int): UserInfo
}
