package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.course.CourseInfoDTO
import vn.tutorme.mobile.domain.model.course.CourseInfo

class CourseInfoListDTOConvertCourseInfoList : IConverter<List<CourseInfoDTO>, List<CourseInfo>> {
    override fun convert(source: List<CourseInfoDTO>): List<CourseInfo> {
        val list = mutableListOf<CourseInfo>()
        source.forEach {
            list.add(CourseInfo(
                courseId = it.id,
                banner = it.banner,
                title = it.title,
                ratingTotal = it.rating,
                ratingNumber = it.studentRegistered,
                memberRegister = it.studentRegistered,
                timeLesson = it.createDate,
                dateEnd = it.endDate,
                price = it.price
            ))
        }

        return list
    }
}
