package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.course.CourseInfoDTO
import vn.tutorme.mobile.domain.model.course.CourseInfo

class CourseInfoDTOConvertCourseInfo : IConverter<CourseInfoDTO, CourseInfo> {
    override fun convert(source: CourseInfoDTO): CourseInfo {
        return CourseInfo(
            courseId = source.id,
            banner = source.banner,
            title = source.title,
            ratingTotal = source.rating,
            ratingNumber = source.studentRegistered,
            memberRegister = source.studentRegistered,
            timeLesson = source.timeLesson,
            dateEnd = source.endDate,
            classCode = source.classCode,
            price = source.price,
            createTime = source.createDate,
            content = source.content,
            subject = source.subject,
            demoClass = source.demoClass
        )
    }
}
