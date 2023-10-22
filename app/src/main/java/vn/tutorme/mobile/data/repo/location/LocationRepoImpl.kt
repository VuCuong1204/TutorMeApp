package vn.tutorme.mobile.data.repo.location

import vn.tutorme.mobile.base.common.exception.ApiException
import vn.tutorme.mobile.data.repo.convert.LocationDTOConvertToLocationInfo
import vn.tutorme.mobile.data.repo.convert.SchoolDTOConvertToSchoolInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeLocationService
import vn.tutorme.mobile.data.source.remote.service.ILocationService
import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo
import vn.tutorme.mobile.domain.repo.ILocationRepo
import javax.inject.Inject

class LocationRepoImpl @Inject constructor() : ILocationRepo, IRepo {
    override fun getDistrictList(provinceId: String): List<LocationInfo> {
        val server = invokeLocationService(ILocationService::class.java)
        val data = server.getDistrictList(provinceId).execute()
        return if (data.isSuccessful) {
            LocationDTOConvertToLocationInfo().convert(data.body()?.data ?: listOf())
        } else {
            throw ApiException(ApiException.TIME_OUT_ERROR)
        }
    }

    override fun getSchoolList(provinceId: String, districtId: String): List<SchoolInfo> {
        val server = invokeLocationService(ILocationService::class.java)
        val data = server.getSchoolList(provinceId, districtId).execute()
        return if (data.isSuccessful) {
            SchoolDTOConvertToSchoolInfo().convert(data.body()?.data ?: listOf())
        } else {
            throw ApiException(ApiException.TIME_OUT_ERROR)
        }
    }
}
