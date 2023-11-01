package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class FeedBackLessonUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<FeedBackLessonUseCase.FeedBackLessonRV, Boolean>() {
    override suspend fun execute(rv: FeedBackLessonRV): Boolean {
        return lessonRepo.feedBackLesson(rv.lessonId, rv.content)
    }

    class FeedBackLessonRV(val lessonId: Int, val content: String) : RequestValue
}
