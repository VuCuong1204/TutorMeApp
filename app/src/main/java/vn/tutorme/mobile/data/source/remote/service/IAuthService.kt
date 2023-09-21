package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.UserInfoResponse

interface IAuthService : IApiService {

    @POST("register")
    fun register(@Query("id") id: String): Call<UserInfoResponse>

    @POST("login")
    fun login(@Query("id") id: String): Call<UserInfoResponse>
}
