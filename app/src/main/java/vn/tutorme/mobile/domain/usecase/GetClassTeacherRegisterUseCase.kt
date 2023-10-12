package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetClassTeacherRegisterUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetClassTeacherRegisterUseCase.GetClassTeacherRegisterRV, List<ClassInfo>>() {
    override suspend fun execute(rv: GetClassTeacherRegisterRV): List<ClassInfo> {
        return lessonRepo.getClassInfoRegisterTeach(rv.currentTime, rv.state, rv.teacherId)
    }

    class GetClassTeacherRegisterRV(val currentTime: Long, val state: Int, val teacherId: String) : RequestValue
}
