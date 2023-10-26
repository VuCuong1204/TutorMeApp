package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.classinfo.ClassInfoDTO
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS
import vn.tutorme.mobile.domain.model.clazz.ClassInfo

class ClassInfoListDTOConvertToClassListInfo : IConverter<List<ClassInfoDTO>, List<ClassInfo>> {
    override fun convert(source: List<ClassInfoDTO>): List<ClassInfo> {
        val list = mutableListOf<ClassInfo>()
        source.forEach {
            list.add(ClassInfo(
                classId = it.id,
                timeBegin = it.timeBegin,
                classStatus = CLASS_STATUS.EMPTY_CLASS_STATUS,
                teacherId = it.teacherId,
                level = it.describeClass,
                courseId = it.courseId,
                titleClass = it.nameClass,
                totalStudent = it.countStudent,
                totalLesson = it.countLesson,
                lessonFirst = it.lessonFirst,
                lessonSecond = it.lessonSecond
            ))
        }

        return list
    }
}
