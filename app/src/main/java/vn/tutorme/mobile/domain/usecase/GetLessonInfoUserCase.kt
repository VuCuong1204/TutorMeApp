package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonInfoUserCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonInfoUserCase.GetLessonInfoRV, List<LessonInfo>>() {
    override suspend fun execute(rv: GetLessonInfoRV): List<LessonInfo> {

        return lessonRepo.getLessonInfoTeacher(rv.id, rv.beginTime, rv.endTime, null, null, null)
    }

    class GetLessonInfoRV(val id: String, var beginTime: Long, val endTime: Long) : RequestValue
}
