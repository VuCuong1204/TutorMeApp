package vn.tutorme.mobile.domain.usecase.course

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.repo.ICourseRepo
import javax.inject.Inject

class GetClassListFromCourseUseCase @Inject constructor(
    private val courseRepo: ICourseRepo
) : BaseUseCase<GetClassListFromCourseUseCase.GetClassListFromCourseRV, List<ClassInfo>>() {
    override suspend fun execute(rv: GetClassListFromCourseRV): List<ClassInfo> {
        return courseRepo.getClassListFromCourse(rv.courseId)
    }

    class GetClassListFromCourseRV(val courseId: String) : RequestValue
}
