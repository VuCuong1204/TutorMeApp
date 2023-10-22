package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.location.LocationDTO
import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo

class LocationDTOConvertToLocationInfo : IConverter<List<LocationDTO>, List<LocationInfo>> {
    override fun convert(source: List<LocationDTO>): List<LocationInfo> {
        val list = mutableListOf<LocationInfo>()
        source.forEach {
            list.add(LocationInfo(
                locationId = it.locationId,
                name = it.name
            ))
        }

        return list
    }
}
