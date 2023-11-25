package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonInfoTeacherUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonInfoTeacherUseCase.GetLessonInfoTeacherRV, List<LessonInfo>>() {
    override suspend fun execute(rv: GetLessonInfoTeacherRV): List<LessonInfo> {
        return lessonRepo.getLessonInfoTeacher(
            id = AppPreferences.userInfo?.userId ?: STRING_DEFAULT,
            timeBegin = rv.startTime,
            timeEnd = rv.endTime,
            stateRate = null,
            page = null,
            size = null
        )
    }

    class GetLessonInfoTeacherRV() : BaseUseCase.RequestValue {
        var startTime: Long = LONG_DEFAULT
        var endTime: Long = LONG_DEFAULT
    }
}
