package vn.tutorme.mobile.presenter.profile.editprofile

import androidx.fragment.app.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.EditProfileFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.profile.provinces.LocationInfo
import vn.tutorme.mobile.domain.model.profile.provinces.ProvincesMain
import vn.tutorme.mobile.domain.model.profile.provinces.SchoolInfo
import vn.tutorme.mobile.presenter.dialog.bottomsheetpicker.BottomSheetPickerDialog
import vn.tutorme.mobile.presenter.dialog.bottomsheetpicker.TYPE_PICKER
import vn.tutorme.mobile.presenter.dialog.datepicker.DATE_TYPE
import vn.tutorme.mobile.presenter.dialog.datepicker.DatePickerDialog
import vn.tutorme.mobile.presenter.profile.model.BottomSheetPicker
import vn.tutorme.mobile.utils.TimeUtils

@AndroidEntryPoint
class EditProfileFragment : TutorMeFragment<EditProfileFragmentBinding>(R.layout.edit_profile_fragment) {

    private val viewModel by viewModels<EditProfileViewModel>()

    override fun onInitView() {
        super.onInitView()
        addHeader()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.districtState) {
            handleUiState(it, object : IViewListener {

                override fun onFailure() {
                    showError(getAppString(R.string.error_msg))
                }

                override fun onSuccess() {
                    if (it.data.isNullOrEmpty()) {
                        showError(getAppString(R.string.error_msg))
                    } else {
                        showDistrictDialog(it.data!!)
                    }
                }
            })
        }

        coroutinesLaunch(viewModel.schoolState) {
            handleUiState(it, object : IViewListener {

                override fun onFailure() {
                    showError(getAppString(R.string.error_msg))
                }

                override fun onSuccess() {
                    if (it.data.isNullOrEmpty()) {
                        showError(getAppString(R.string.error_msg))
                    } else {
                        showSchoolDialog(it.data!!)
                    }
                }
            })
        }
    }

    private fun addHeader() {
        binding.ivEditEditProfileBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.edtEditProfileUserName.setText(AppPreferences.userInfo?.fullName ?: STRING_DEFAULT)
        binding.edtEditProfilePhone.setText("${AppPreferences.userInfo?.phoneNumber}")
        binding.edtEditProfileGender.text = AppPreferences.userInfo?.getGenderUser()
        binding.edtEditProfileDate.text = AppPreferences.userInfo?.date
        if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
            binding.edtEditProfileSchool.text = AppPreferences.userInfo?.nameSchool
        } else {
            binding.edtEditProfileSchool.gone()
            binding.tvEditProfileSchool.gone()
        }

        binding.tvEditProfileConfirm.setOnSafeClick { }
        binding.edtEditProfileGender.setOnSafeClick { showGenderDialog() }
        binding.edtEditProfileDate.setOnSafeClick { showDateDialog() }
        binding.edtEditProfileProvince.setOnSafeClick { showProvincesDialog() }
        binding.edtEditProfileDistrict.setOnSafeClick {
            if (viewModel.provincesId == null) {
                showError(getAppString(R.string.input_province_error))
            } else {
                viewModel.getDistrictList(viewModel.provincesId!!)
            }
        }
        binding.edtEditProfileSchool.setOnSafeClick {
            if (viewModel.provincesId == null) {
                showError(getAppString(R.string.input_province_error))
            } else if (viewModel.districtId == null) {
                showError(getAppString(R.string.input_district_error))
            } else {
                viewModel.getSchoolList(viewModel.provincesId!!, viewModel.districtId!!)
            }
        }
    }

    private fun showDateDialog() {
        val dialog = DatePickerDialog(datePickerListener = {
            val content = TimeUtils.convertLongToString(it.timeInMillis, TimeUtils.DATE_FORMAT_V2)
            binding.edtEditProfileDate.text = content
        }, dateType = DATE_TYPE.PASS)
        dialog.show(childFragmentManager, TAG)
    }

    private fun showGenderDialog() {
        val genderList = mutableListOf(
            getAppString(R.string.male),
            getAppString(R.string.female),
            getAppString(R.string.other),
        )

        val dialog = BottomSheetPickerDialog
            .Builder()
            .setType(TYPE_PICKER.GENDER)
            .setListPicker(genderList.map {
                BottomSheetPicker().apply {
                    title = it
                    optionalData = it
                    isSelected = if (viewModel.genderName == null)
                        AppPreferences.userInfo?.getGenderUser() == optionalData
                    else viewModel.genderName == optionalData
                }
            })
            .setRatioDialogHeight(0.35f)
            .setTitle(getAppString(R.string.select_gender))
            .setChooseItemListener {
                val genderString = (it.optionalData as? String)
                binding.edtEditProfileGender.text = genderString
                viewModel.genderName = genderString
            }
        dialog.show(childFragmentManager)
    }

    private fun showProvincesDialog() {
        val provincesList = getDataFromJson().data ?: listOf()

        val dialog = BottomSheetPickerDialog
            .Builder()
            .setType(TYPE_PICKER.DEFAULT)
            .setListPicker(provincesList.map {
                BottomSheetPicker().apply {
                    title = it.name
                    optionalData = it.locationId
                    isSelected = viewModel.provincesId == optionalData
                }
            })
            .setRatioDialogHeight(0.35f)
            .setTitle(getAppString(R.string.select_province))
            .setChooseItemListener {
                binding.edtEditProfileProvince.text = it.title
                viewModel.provincesId = it.optionalData as? String
            }
        dialog.show(childFragmentManager)
    }

    private fun showDistrictDialog(list: List<LocationInfo>) {
        val dialog = BottomSheetPickerDialog
            .Builder()
            .setType(TYPE_PICKER.DEFAULT)
            .setListPicker(list.map {
                BottomSheetPicker().apply {
                    title = it.name
                    optionalData = it.locationId
                    isSelected = viewModel.districtId == optionalData
                }
            })
            .setRatioDialogHeight(0.35f)
            .setTitle(getAppString(R.string.select_district))
            .setChooseItemListener {
                binding.edtEditProfileDistrict.text = it.title
                viewModel.districtId = it.optionalData as? String
            }
        dialog.show(childFragmentManager)
    }

    private fun showSchoolDialog(list: List<SchoolInfo>) {
        val dialog = BottomSheetPickerDialog
            .Builder()
            .setType(TYPE_PICKER.DEFAULT)
            .setListPicker(list.map {
                BottomSheetPicker().apply {
                    title = it.schoolName
                    optionalData = it.schoolId
                    isSelected = viewModel.schoolId == optionalData
                }
            })
            .setRatioDialogHeight(0.35f)
            .setTitle(getAppString(R.string.select_district))
            .setChooseItemListener {
                binding.edtEditProfileSchool.text = it.title
                viewModel.schoolId = it.optionalData as? String
            }
        dialog.show(childFragmentManager)
    }

    private fun getDataFromJson(): ProvincesMain {
        val jsonInputStream = resources.openRawResource(R.raw.provinces)
        val jsonText = jsonInputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonText, ProvincesMain::class.java)
    }
}
