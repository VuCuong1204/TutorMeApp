package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.tensorflow.DetectInfoResponse
import vn.tutorme.mobile.domain.model.detectinfo.DetectInfo

class DetectInfoResponseDetectInfo : IConverter<DetectInfoResponse, DetectInfo> {
    override fun convert(source: DetectInfoResponse): DetectInfo {
        return DetectInfo(
            confidence = source.confidence,
            userId = source.name
        )
    }
}
