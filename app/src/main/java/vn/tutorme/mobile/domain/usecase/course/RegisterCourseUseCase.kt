package vn.tutorme.mobile.domain.usecase.course

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ICourseRepo
import javax.inject.Inject

class RegisterCourseUseCase @Inject constructor(
    private val courseRepo: ICourseRepo
) : BaseUseCase<RegisterCourseUseCase.RegisterCourseRV, Boolean>() {
    override suspend fun execute(rv: RegisterCourseRV): Boolean {
        return courseRepo.registerCourse(rv.classId, rv.studentId, rv.lessonFirst, rv.lessonSecond)
    }

    class RegisterCourseRV(
        val classId: String,
        val studentId: String,
        val lessonFirst: Int,
        val lessonSecond: Int
    ) : RequestValue
}
