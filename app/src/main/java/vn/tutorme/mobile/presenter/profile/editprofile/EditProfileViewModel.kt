package vn.tutorme.mobile.presenter.profile.editprofile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo
import vn.tutorme.mobile.domain.usecase.location.GetDistrictListUseCase
import vn.tutorme.mobile.domain.usecase.location.GetSchoolListUseCase
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getDistrictListUseCase: GetDistrictListUseCase,
    private val getSchoolListUseCase: GetSchoolListUseCase
) : BaseViewModel() {
    private val _districtState = MutableStateFlow(FlowResult.newInstance<List<LocationInfo>>())
    val districtState = _districtState.asStateFlow()

    private val _schoolState = MutableStateFlow(FlowResult.newInstance<List<SchoolInfo>>())
    val schoolState = _schoolState.asStateFlow()

    var genderName: String? = null
    var provincesId: String? = null
    var districtId: String? = null
    var schoolId: String? = null

    fun getDistrictList(provinceId: String) {
        viewModelScope.launch {
            val rv = GetDistrictListUseCase.GetDistrictListRV(provinceId)
            getDistrictListUseCase.invoke(rv)
                .onException {
                    _districtState.failure(it)
                }
                .collect {
                    _districtState.success(it)
                }
        }
    }

    fun getSchoolList(provinceId: String, districtId: String) {
        viewModelScope.launch {
            val rv = GetSchoolListUseCase.GetSchoolListRV(provinceId, districtId)
            getSchoolListUseCase.invoke(rv)
                .onException {
                    _schoolState.failure(it)
                }
                .collect {
                    _schoolState.success(it)
                }
        }
    }
}
