package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonInfoStudentUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonInfoStudentUseCase.GetLessonInfoStudentRV, List<LessonInfo>>() {
    override suspend fun execute(rv: GetLessonInfoStudentRV): List<LessonInfo> {
        return lessonRepo.getLessonStudentInDay(
            AppPreferences.userInfo?.userId ?: STRING_DEFAULT,
            rv.startTime,
            rv.endTime,
            null,
            null
        )
    }

    class GetLessonInfoStudentRV : RequestValue {
        var startTime: Long = LONG_DEFAULT
        var endTime: Long = LONG_DEFAULT
    }
}
