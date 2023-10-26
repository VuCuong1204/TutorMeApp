package vn.tutorme.mobile.domain.usecase.course

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ICourseRepo
import javax.inject.Inject

class CheckCourseRegisteredUseCase @Inject constructor(
    private val courseRepo: ICourseRepo
) : BaseUseCase<CheckCourseRegisteredUseCase.CheckCourseRegisteredRV, Boolean>() {
    override suspend fun execute(rv: CheckCourseRegisteredRV): Boolean {
        return courseRepo.checkCourseRegistered(rv.courseId, rv.userId)
    }

    class CheckCourseRegisteredRV(val courseId: String, val userId: String) : BaseUseCase.RequestValue
}
