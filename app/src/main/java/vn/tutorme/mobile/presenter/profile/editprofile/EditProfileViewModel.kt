package vn.tutorme.mobile.presenter.profile.editprofile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo
import vn.tutorme.mobile.domain.usecase.location.GetDistrictListUseCase
import vn.tutorme.mobile.domain.usecase.location.GetSchoolListUseCase
import vn.tutorme.mobile.domain.usecase.profile.UpdateProfileUseCase
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getDistrictListUseCase: GetDistrictListUseCase,
    private val getSchoolListUseCase: GetSchoolListUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {
    private val _districtState = MutableStateFlow(FlowResult.newInstance<List<LocationInfo>>())
    val districtState = _districtState.asStateFlow()

    private val _schoolState = MutableStateFlow(FlowResult.newInstance<List<SchoolInfo>>())
    val schoolState = _schoolState.asStateFlow()

    private val _profileState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val profileState = _profileState.asStateFlow()

    var genderName: String? = null
    var provincesId: String? = null
    var districtId: String? = null
    var schoolId: String? = null
    var avatarLink: String? = null

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

    fun updateProfile(
        fullName: String? = STRING_DEFAULT,
        date: String? = STRING_DEFAULT,
        address: String? = STRING_DEFAULT,
        nameSchool: String? = STRING_DEFAULT,
        gender: GENDER_TYPE = getGenderType(genderName),
        phoneNumber: Long? = LONG_DEFAULT,
        avatar: String = avatarLink ?: STRING_DEFAULT) {
        viewModelScope.launch {
            val rv = UpdateProfileUseCase.UpdateProfileRV().apply {
                this.fullName = fullName!!
                this.date = date!!
                this.address = address!!
                this.nameSchool = nameSchool!!
                this.gender = gender
                this.phoneNumber = phoneNumber!!
                this.avatar = avatar
            }

            updateProfileUseCase.invoke(rv)
                .onStart {
                    _profileState.loading()
                }
                .onException {
                    _profileState.failure(it)
                }
                .collect {
                    _profileState.success(it)
                }
        }
    }

    private fun getGenderType(gender: String?): GENDER_TYPE {
        return when (gender) {
            getAppString(R.string.male) -> GENDER_TYPE.MALE_TYPE
            getAppString(R.string.female) -> GENDER_TYPE.FEMALE_TYPE
            getAppString(R.string.other) -> GENDER_TYPE.OTHER
            else -> GENDER_TYPE.OTHER
        }
    }
}
