package vn.tutorme.mobile.domain.usecase.course

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.repo.ICourseRepo
import javax.inject.Inject

class GetCourseInfoUseCase @Inject constructor(
    private val courseRepo: ICourseRepo
) : BaseUseCase<GetCourseInfoUseCase.GetCourseInfoRV, CourseInfo>() {
    override suspend fun execute(rv: GetCourseInfoRV): CourseInfo {
        return courseRepo.getCourseInfo(rv.courseId)
    }

    class GetCourseInfoRV(val courseId: String) : BaseUseCase.RequestValue
}
