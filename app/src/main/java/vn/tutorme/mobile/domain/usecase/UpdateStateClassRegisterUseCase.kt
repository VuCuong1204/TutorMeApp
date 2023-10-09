package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class UpdateStateClassRegisterUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<UpdateStateClassRegisterUseCase.UpdateStateClassRegisterRV, List<ClassInfo>>() {
    override suspend fun execute(rv: UpdateStateClassRegisterRV): List<ClassInfo> {
        return lessonRepo.updateStateClassRegister(rv.classId, rv.state, rv.teacherId)
    }

    class UpdateStateClassRegisterRV(val classId: String, val state: Int, val teacherId: String) : BaseUseCase.RequestValue
}
