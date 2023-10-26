package vn.tutorme.mobile.data.repo.lesson

import vn.tutorme.mobile.data.repo.convert.ClassMainDTOConvertToClassInfo
import vn.tutorme.mobile.data.repo.convert.CourseInfoListDTOConvertCourseInfoList
import vn.tutorme.mobile.data.repo.convert.LessonMainDTOConvertLessonInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.service.ILessonService
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class LessonRepoImpl @Inject constructor() : ILessonRepo, IRepo {
    override fun getMissionInfo(id: String, timeBegin: Long, timeEnd: Long): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonList(id, timeBegin, timeEnd, null, null, null).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfo().convert(body.data!!)
        }
    }

    override fun getLessonInfoTeacher(id: String, timeBegin: Long, timeEnd: Long, stateRate: Int?, page: Int?, size: Int?): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonList(id, timeBegin, timeEnd, stateRate, page, size).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfo().convert(body.data!!)
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
            LessonMainDTOConvertLessonInfo().convert(body.data!!)
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
            CourseInfoListDTOConvertCourseInfoList().convert(body.data!!)
        }
    }

    override fun getLessonListInClass(classId: String, page: Int?, size: Int?): List<LessonInfo> {
        val service = invokeAuthService(ILessonService::class.java)

        return service.getLessonStudentInClass(classId, page, size).invokeApi { _, body ->
            LessonMainDTOConvertLessonInfo().convert(body.data!!)
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
}
