package vn.tutorme.mobile.data.repo.lesson

import vn.tutorme.mobile.base.common.converter.ListConverter
import vn.tutorme.mobile.data.repo.convert.ClassMainDTOConvertToClassInfo
import vn.tutorme.mobile.data.repo.convert.CourseInfoDTOConvertCourseInfo
import vn.tutorme.mobile.data.repo.convert.FeedBackInfoDTOConvertFeedBackInfo
import vn.tutorme.mobile.data.repo.convert.LessonInfoDTOConvertLessonInfoList
import vn.tutorme.mobile.data.repo.convert.LessonMainDTOConvertLessonInfoList
import vn.tutorme.mobile.data.repo.convert.UserInfoDTOConvertToUserInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.service.ILessonService
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.model.feedback.FeedBackInfo
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class LessonRepoImpl @Inject constructor() : ILessonRepo, IRepo {
    override fun getMissionInfo(id: String, timeBegin: Long, timeEnd: Long): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonList(id, timeBegin, timeEnd, null, null, null).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfoList().convert(body.data!!)
        }
    }

    override fun getLessonInfoTeacher(id: String, timeBegin: Long, timeEnd: Long, stateRate: Int?, page: Int?, size: Int?): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonList(id, timeBegin, timeEnd, stateRate, page, size).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfoList().convert(body.data!!)
        }
    }

    override fun getClassInfoRegisterTeach(currentTime: Long?, state: Int, teacherId: String?): List<ClassInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getClassTeacherRegistered(currentTime, state, teacherId).invokeApi { _, body ->
            ClassMainDTOConvertToClassInfo().convert(body.data!!)
        }
    }

    override fun getLessonStudentInDay(studentId: String, beginTime: Long, endTime: Long, page: Int?, size: Int?): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonStudentInDay(studentId, beginTime, endTime, page, size).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfoList().convert(body.data!!)
        }
    }

    override fun getClassStudentRegistered(studentId: String, page: Int?, size: Int?): List<ClassInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getClassStudentRegistered(studentId, page, size).invokeApi { _, body ->
            ClassMainDTOConvertToClassInfo().convert(body.data!!)
        }
    }

    override fun getCourseList(currentTime: Long, page: Int?, size: Int?): List<CourseInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getCourseList(currentTime, page, size).invokeApi { _, body ->
            CourseInfoDTOConvertCourseInfo().convert(body.data!!)
        }
    }

    override fun getLessonListInClass(classId: String, page: Int?, size: Int?): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonStudentInClass(classId, page, size).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfoList().convert(body.data!!)
        }
    }

    override fun updateStateClassRegister(classId: String, state: Int, teacherId: String): List<ClassInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.updateStateClassRegister(classId, state, teacherId).invokeApi { _, body ->
            ClassMainDTOConvertToClassInfo().convert(body.data!!)
        }
    }

    override fun getClassTeacherList(id: String, type: Int, page: Int?, size: Int?): List<ClassInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getClassTeacherList(id, type, page, size).invokeApi { _, body ->
            ClassMainDTOConvertToClassInfo().convert(body.data!!)
        }
    }

    override fun getLessonDetail(lessonId: Int): LessonInfo {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonDetail(lessonId).invokeApi { _, body ->
            LessonInfoDTOConvertLessonInfoList().convert(body.data!!)
        }
    }

    override fun getStudentInLesson(classId: String): List<UserInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getStudentInLesson(classId).invokeApi { _, body ->
            ListConverter(UserInfoDTOConvertToUserInfo()).invoke(body.data!!)
        }
    }

    override fun attendanceStudent(lessonId: Int, studentId: String): Boolean {
        val service = invokeAuthService(ILessonService::class.java)

        return service.attendanceStudent(lessonId, studentId).invokeApi { _, _ ->
            true
        }
    }

    override fun updateStateLesson(lessonId: Int, state: Int): LessonInfo {
        val service = invokeAuthService(ILessonService::class.java)

        return service.updateStateLesson(lessonId, state).invokeApi { _, body ->
            LessonInfoDTOConvertLessonInfoList().convert(body.data!!)
        }
    }

    override fun feedBackLesson(lessonId: Int, content: String): Boolean {
        val service = invokeAuthService(ILessonService::class.java)

        return service.feedBackLesson(lessonId, content).invokeApi { _, _ ->
            true
        }
    }

    override fun getFeedbackList(lessonId: Int): List<FeedBackInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getFeedbackList(lessonId).invokeApi { _, body ->
            ListConverter(FeedBackInfoDTOConvertFeedBackInfo()).invoke(body.data!!)
        }
    }
}
