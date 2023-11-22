package vn.tutorme.mobile.data.repo.tensorflow

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import vn.tutorme.mobile.base.common.exception.ApiException
import vn.tutorme.mobile.base.common.exception.ApiException.Companion.ERROR_DETECT_FACE
import vn.tutorme.mobile.data.repo.convert.DetectInfoResponseDetectInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeTenserFlowService
import vn.tutorme.mobile.data.source.remote.model.tensorflow.DetectInfoResponse
import vn.tutorme.mobile.data.source.remote.service.ITenserFlowService
import vn.tutorme.mobile.domain.model.detectinfo.DetectInfo
import vn.tutorme.mobile.domain.repo.ITensorflowRepo
import java.io.File
import javax.inject.Inject

class TensorflowRepoImpl @Inject constructor() : IRepo, ITensorflowRepo {

    companion object {
        private const val PART_FILE = "file"
    }

    override fun sendFaceDetectImage(file: String): DetectInfo {
        val service = invokeTenserFlowService(ITenserFlowService::class.java)
        val fileImage = convertFileToMultipart(PART_FILE, file)
        val data = service.sendFaceDetectImage(fileImage).execute()
        if (data.isSuccessful) {
            return DetectInfoResponseDetectInfo().convert(data.body() ?: DetectInfoResponse())
        } else {
            data.errorBody()
            data.message()
            data.body()
            throw ApiException(ERROR_DETECT_FACE)
        }
    }

    private fun convertFileToMultipart(key: String, absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(key, file.name, file.asRequestBody())
    }
}
