package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.feedback.FeedbackInfoDTO
import vn.tutorme.mobile.domain.model.feedback.FeedBackInfo

class FeedBackInfoDTOConvertFeedBackInfo : IConverter<FeedbackInfoDTO, FeedBackInfo> {
    override fun convert(source: FeedbackInfoDTO): FeedBackInfo {
        return FeedBackInfo(
            id = source.id,
            rating = source.rating,
            comment = source.comment,
            lessonId = source.lessonId
        )
    }
}
