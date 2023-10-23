package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.user.UserInfoRequest
import vn.tutorme.mobile.data.source.remote.model.user.UserInfoResponse

interface IAuthService : IApiService {

    @POST("register")
    fun register(@Query("id") id: String): Call<UserInfoResponse>

    @POST("login")
    fun login(@Query("id") id: String): Call<UserInfoResponse>

    @POST("update/profile")
    fun updateProfile(@Body userInfoRequest: UserInfoRequest): Call<BaseApiResponse>
}
