package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.location.LocationResponse
import vn.tutorme.mobile.data.source.remote.model.location.SchoolResponse

interface ILocationService : IApiService {

    @GET("api/v1/social/fqa/location/province/{provinceId}/districts")
    fun getDistrictList(@Path("provinceId") provinceId: String): Call<LocationResponse>

    @GET("api/v1/social/fqa/location/province/{provinceId}/district/{districtId}/schools")
    fun getSchoolList(
        @Path("provinceId") provinceId: String,
        @Path("districtId") districtId: String
    ): Call<SchoolResponse>
}
