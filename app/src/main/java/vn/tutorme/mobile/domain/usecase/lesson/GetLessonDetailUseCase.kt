package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonDetailUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonDetailUseCase.GetLessonDetailRV, LessonInfo>() {
    override suspend fun execute(rv: GetLessonDetailRV): LessonInfo {
        return lessonRepo.getLessonDetail(rv.lessonId)
    }

    class GetLessonDetailRV(val lessonId: Int) : RequestValue
}
