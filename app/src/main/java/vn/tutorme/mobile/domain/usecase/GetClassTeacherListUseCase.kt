package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.LIMIT_SIZE
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import vn.tutorme.mobile.presenter.classmanager.CLASS_TYPE
import javax.inject.Inject

class GetClassTeacherListUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetClassTeacherListUseCase.GetClassTeacherListRV, List<ClassInfo>>() {
    override suspend fun execute(rv: GetClassTeacherListRV): List<ClassInfo> {
        return lessonRepo.getClassTeacherList(rv.id, rv.type.value, rv.page, rv.size)
    }

    class GetClassTeacherListRV(val id: String) : RequestValue {
        var type = CLASS_TYPE.REGULAR_TYPE
        var page: Int = INT_DEFAULT
        var size: Int = LIMIT_SIZE
    }
}
