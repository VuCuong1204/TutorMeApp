package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetStudentInLessonUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetStudentInLessonUseCase.GetStudentInLessonRV, List<UserInfo>>() {
    override suspend fun execute(rv: GetStudentInLessonRV): List<UserInfo> {
        return lessonRepo.getStudentInLesson(rv.classId)
    }

    class GetStudentInLessonRV(val classId: String) : RequestValue
}
