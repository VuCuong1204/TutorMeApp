package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.data.source.remote.model.lesson.LessonInfoMainDTO
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LESSON_TYPE
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

class LessonMainDTOConvertLessonInfo : IConverter<List<LessonInfoMainDTO>, List<LessonInfo>> {

    override fun convert(source: List<LessonInfoMainDTO>): List<LessonInfo> {
        val list = mutableListOf<LessonInfo>()
        source.forEach {
            list.add(LessonInfo(
                lessonId = it.lessonInfo?.id,
                classId = it.classInfo?.id,
                timeBegin = it.lessonInfo?.beginTime,
                timeEnd = it.lessonInfo?.endTime,
                status = LESSON_STATUS.valuesOfName(it.lessonInfo?.state ?: INT_DEFAULT),
                nameClass = it.classInfo?.nameClass,
                level = it.classInfo?.describeClass,
                totalNumber = it.classInfo?.countStudent,
                lessonSession = it.classInfo?.countLesson,
                type = LESSON_TYPE.valueOfName(it.lessonInfo?.updateAssessment),
                memberNumber = it.lessonInfo?.countAttendance,
                countAssessment = it.lessonInfo?.countReview
            ))
        }

        return list
    }
}
