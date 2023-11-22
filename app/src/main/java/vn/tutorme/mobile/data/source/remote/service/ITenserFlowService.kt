package vn.tutorme.mobile.data.source.remote.service

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.tensorflow.DetectInfoResponse

interface ITenserFlowService : IApiService {

    @Multipart
    @POST("detect/image")
    fun sendFaceDetectImage(
        @Part file: MultipartBody.Part
    ): Call<DetectInfoResponse>
}
