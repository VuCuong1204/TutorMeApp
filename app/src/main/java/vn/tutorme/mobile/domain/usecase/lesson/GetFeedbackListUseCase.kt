package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.feedback.FeedBackInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetFeedbackListUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetFeedbackListUseCase.GetFeedbackListRV, List<FeedBackInfo>>() {
    override suspend fun execute(rv: GetFeedbackListRV): List<FeedBackInfo> {
        return lessonRepo.getFeedbackList(rv.lessonId)
    }

    class GetFeedbackListRV(val lessonId: Int) : RequestValue
}
