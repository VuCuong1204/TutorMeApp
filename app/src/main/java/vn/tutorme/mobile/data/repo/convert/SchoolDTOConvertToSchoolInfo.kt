package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.location.SchoolDTO
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo

class SchoolDTOConvertToSchoolInfo : IConverter<List<SchoolDTO>, List<SchoolInfo>> {
    override fun convert(source: List<SchoolDTO>): List<SchoolInfo> {
        val list = mutableListOf<SchoolInfo>()
        source.forEach {
            list.add(SchoolInfo(
                schoolId = it.schoolId,
                provincesCityId = it.provincesCityId,
                districtId = it.districtId,
                schoolName = it.schoolName,
                level = it.level
            ))
        }

        return list
    }
}
