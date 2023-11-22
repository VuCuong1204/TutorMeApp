package vn.tutorme.mobile.domain.usecase.tensorflow

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.detectinfo.DetectInfo
import vn.tutorme.mobile.domain.repo.ITensorflowRepo
import javax.inject.Inject

class SendFaceDetectImageUseCase @Inject constructor(
    private val tensorflowRepo: ITensorflowRepo
) : BaseUseCase<SendFaceDetectImageUseCase.SendFaceDetectImageRV, DetectInfo>() {
    override suspend fun execute(rv: SendFaceDetectImageRV): DetectInfo {
        return tensorflowRepo.sendFaceDetectImage(rv.uri)
    }

    class SendFaceDetectImageRV(val uri: String) : RequestValue
}
