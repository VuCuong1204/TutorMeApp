package vn.tutorme.mobile.domain.usecase.location

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo
import vn.tutorme.mobile.domain.repo.ILocationRepo
import javax.inject.Inject

class GetDistrictListUseCase @Inject constructor(
    private val locationRepo: ILocationRepo
) : BaseUseCase<GetDistrictListUseCase.GetDistrictListRV, List<LocationInfo>>() {
    override suspend fun execute(rv: GetDistrictListRV): List<LocationInfo> {
        return locationRepo.getDistrictList(rv.provinceId)
    }

    class GetDistrictListRV(val provinceId: String) : BaseUseCase.RequestValue
}
