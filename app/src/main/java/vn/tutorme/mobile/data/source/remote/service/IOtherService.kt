package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.videocall.VideoInfoResponse

interface IOtherService : IApiService {

    @GET("access_token_for_client.php")
    fun getAccessTokenVideoCall(@Query("u") id: String): Call<VideoInfoResponse>
}
