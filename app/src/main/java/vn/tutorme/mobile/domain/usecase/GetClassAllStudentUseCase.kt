package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetClassAllStudentUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetClassAllStudentUseCase.GetClassAllStudentVH, List<ClassInfo>>() {
    override suspend fun execute(rv: GetClassAllStudentVH): List<ClassInfo> {

        return lessonRepo.getClassStudentRegistered(
            rv.studentId,
            rv.page,
            rv.size
        )
    }

    class GetClassAllStudentVH(val studentId: String) : RequestValue {
        var page: Int? = Extension.INT_DEFAULT
        var size: Int? = Extension.LIMIT_SIZE
    }
}
