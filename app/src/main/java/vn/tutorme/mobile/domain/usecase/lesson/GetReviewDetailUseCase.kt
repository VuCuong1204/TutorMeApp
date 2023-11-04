package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetReviewDetailUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetReviewDetailUseCase.GetReviewDetailRV, UserInfo>() {
    override suspend fun execute(rv: GetReviewDetailRV): UserInfo {
        return lessonRepo.getReviewDetail(rv.userId, rv.lessonId)
    }

    class GetReviewDetailRV(val userId: String, val lessonId: Int) : RequestValue
}
