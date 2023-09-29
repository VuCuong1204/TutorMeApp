package vn.tutorme.mobile.data.source.remote.model.lesson

import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse

data class LessonInfoResponse(
    var data: List<LessonInfoMainDTO>? = null
) : BaseApiResponse()
