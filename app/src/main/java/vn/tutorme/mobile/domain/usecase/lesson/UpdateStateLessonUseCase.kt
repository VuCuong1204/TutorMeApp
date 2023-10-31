package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class UpdateStateLessonUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<UpdateStateLessonUseCase.UpdateStateLessonRV, LessonInfo>() {
    override suspend fun execute(rv: UpdateStateLessonRV): LessonInfo {
        return lessonRepo.updateStateLesson(rv.lessonId, rv.state.value)
    }

    class UpdateStateLessonRV(val lessonId: Int, val state: LESSON_STATUS) : RequestValue
}
