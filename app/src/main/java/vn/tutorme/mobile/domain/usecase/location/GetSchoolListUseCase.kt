package vn.tutorme.mobile.domain.usecase.location

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo
import vn.tutorme.mobile.domain.repo.ILocationRepo
import javax.inject.Inject

class GetSchoolListUseCase @Inject constructor(
    private val locationRepo: ILocationRepo
) : BaseUseCase<GetSchoolListUseCase.GetSchoolListRV, List<SchoolInfo>>() {
    override suspend fun execute(rv: GetSchoolListRV): List<SchoolInfo> {
        return locationRepo.getSchoolList(rv.provinceId, rv.districtId)
    }

    class GetSchoolListRV(val provinceId: String, val districtId: String) : RequestValue
}
