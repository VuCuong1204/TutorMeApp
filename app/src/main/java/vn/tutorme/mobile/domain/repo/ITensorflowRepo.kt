package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.detectinfo.DetectInfo

interface ITensorflowRepo {
    fun sendFaceDetectImage(file: String): DetectInfo
}
