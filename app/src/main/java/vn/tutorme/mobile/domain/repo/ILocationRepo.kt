package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo

interface ILocationRepo {
    fun getDistrictList(provinceId: String): List<LocationInfo>
    fun getSchoolList(provinceId: String, districtId: String): List<SchoolInfo>
}
