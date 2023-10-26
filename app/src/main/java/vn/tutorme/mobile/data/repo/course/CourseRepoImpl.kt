package vn.tutorme.mobile.data.repo.course

import vn.tutorme.mobile.data.repo.convert.BannerEventDTOConvertToBannerEvent
import vn.tutorme.mobile.data.repo.convert.ClassInfoListDTOConvertToClassListInfo
import vn.tutorme.mobile.data.repo.convert.CourseInfoDTOConvertCourseInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.service.ICourseService
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.repo.ICourseRepo
import javax.inject.Inject

class CourseRepoImpl @Inject constructor() : ICourseRepo, IRepo {

    override fun getCourseInfo(courseId: String): CourseInfo {
        val service = invokeAuthService(ICourseService::class.java)
        return service.getCourseInfo(courseId).invokeApi { _, body ->
            CourseInfoDTOConvertCourseInfo().convert(body.data!!)
        }
    }

    override fun getClassListFromCourse(courseId: String): List<ClassInfo> {
        val service = invokeAuthService(ICourseService::class.java)
        return service.getClassListFromCourse(courseId).invokeApi { _, body ->
            ClassInfoListDTOConvertToClassListInfo().convert(body.data!!)
        }
    }

    override fun checkCourseRegistered(courseId: String, userId: String): Boolean {
        val service = invokeAuthService(ICourseService::class.java)
        return service.checkCourseRegistered(courseId, userId).invokeApi { _, body ->
            body.data!!
        }
    }

    override fun registerCourse(classId: String, studentId: String, lessonFirst: Int, lessonSecond: Int): Boolean {
        val service = invokeAuthService(ICourseService::class.java)
        return service.registerCourse(classId, studentId, lessonFirst, lessonSecond).invokeApi { _, _ ->
            true
        }
    }

    override fun getBannerInfoEvent(eventId: Int): BannerEventInfo {
        val service = invokeAuthService(ICourseService::class.java)
        return service.getBannerInfoEvent(eventId).invokeApi { _, body ->
            BannerEventDTOConvertToBannerEvent().convert(body.data!!)
        }
    }
}
