package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.classinfo.ClassInfoMainDTO
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS
import vn.tutorme.mobile.domain.model.clazz.ClassInfo

class ClassMainDTOConvertToClassInfo : IConverter<List<ClassInfoMainDTO>, List<ClassInfo>> {
    override fun convert(source: List<ClassInfoMainDTO>): List<ClassInfo> {
        val list = mutableListOf<ClassInfo>()
        source.forEach {
            list.add(ClassInfo(
                classId = it.classInfo?.id,
                timeBegin = it.classInfo?.timeBegin,
                classStatus = CLASS_STATUS.EMPTY_CLASS_STATUS,
                teacherId = it.classInfo?.teacherId,
                level = it.classInfo?.describeClass,
                titleClass = it.classInfo?.nameClass,
                totalNumber = it.classInfo?.countLesson
            ))
        }

        return list
    }
}
