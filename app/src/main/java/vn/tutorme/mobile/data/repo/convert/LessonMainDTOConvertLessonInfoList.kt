package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.data.source.remote.model.lesson.LessonInfoMainDTO
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LESSON_TYPE
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

class LessonMainDTOConvertLessonInfoList : IConverter<List<LessonInfoMainDTO>, List<LessonInfo>> {

    override fun convert(source: List<LessonInfoMainDTO>): List<LessonInfo> {
        val list = mutableListOf<LessonInfo>()
        source.forEach {
            list.add(LessonInfo(
                lessonId = it.lessonInfo?.id,
                classId = it.classInfo?.id,
                timeBegin = it.classInfo?.timeBegin,
                timeBeginLesson = it.lessonInfo?.beginTime,
                timeEnd = it.lessonInfo?.endTime,
                status = LESSON_STATUS.valuesOfName(it.lessonInfo?.state ?: INT_DEFAULT),
                nameClass = it.classInfo?.nameClass,
                level = it.classInfo?.describeClass,
                totalNumber = it.classInfo?.countStudent,
                lessonSession = it.lessonInfo?.positionLesson,
                type = LESSON_TYPE.valueOfName(it.lessonInfo?.updateAssessment),
                memberNumber = it.lessonInfo?.countAttendance,
                countAssessment = it.lessonInfo?.countReview,
                nameTeacher = it.userInfo?.fullName,
                phoneNumberTeacher = it.userInfo?.phoneNumber
            ))
        }

        return list
    }
}

class LessonInfoDTOConvertLessonInfoList : IConverter<LessonInfoMainDTO, LessonInfo> {
    override fun convert(source: LessonInfoMainDTO): LessonInfo {
        return LessonInfo(
            lessonId = source.lessonInfo?.id,
            classId = source.classInfo?.id,
            timeBegin = source.classInfo?.timeBegin,
            timeBeginLesson = source.lessonInfo?.beginTime,
            timeEnd = source.lessonInfo?.endTime,
            status = LESSON_STATUS.valuesOfName(source.lessonInfo?.state ?: INT_DEFAULT),
            nameClass = source.classInfo?.nameClass,
            level = source.classInfo?.describeClass,
            totalNumber = source.classInfo?.countStudent,
            lessonSession = source.lessonInfo?.positionLesson,
            type = LESSON_TYPE.valueOfName(source.lessonInfo?.updateAssessment),
            memberNumber = source.lessonInfo?.countAttendance,
            countAssessment = source.lessonInfo?.countReview,
            nameTeacher = source.userInfo?.fullName,
            phoneNumberTeacher = source.userInfo?.phoneNumber
        )
    }
}
