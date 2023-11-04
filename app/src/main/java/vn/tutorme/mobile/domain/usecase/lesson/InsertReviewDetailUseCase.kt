package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class InsertReviewDetailUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<InsertReviewDetailUseCase.InsertReviewDetailVH, Boolean>() {
    override suspend fun execute(rv: InsertReviewDetailVH): Boolean {
        return lessonRepo.insertReviewDetail(
            rv.scoreAttitude,
            rv.commentAttitude,
            rv.scorePreparation,
            rv.commentPreparation,
            rv.scoreAskQuestion,
            rv.commentAskQuestion,
            rv.scoreJoinTheDiscussion,
            rv.commentJoinTheDiscussion,
            rv.scoreAttention,
            rv.commentAttention,
            rv.scoreCompleteTheXercise,
            rv.commentCompleteTheXercise,
            rv.commentMedium,
            rv.userId,
            rv.lessonId
        )
    }

    class InsertReviewDetailVH(
        val scoreAttitude: Float,
        val commentAttitude: String,
        val scorePreparation: Float,
        val commentPreparation: String,
        val scoreAskQuestion: Float,
        val commentAskQuestion: String,
        val scoreJoinTheDiscussion: Float,
        val commentJoinTheDiscussion: String,
        val scoreAttention: Float,
        val commentAttention: String,
        val scoreCompleteTheXercise: Float,
        val commentCompleteTheXercise: String,
        val commentMedium: String,
        val userId: String,
        val lessonId: Int
    ) : RequestValue
}
