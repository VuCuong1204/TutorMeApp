package vn.tutorme.mobile.presenter.profile.infomation

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.InformationPersonFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.presenter.profile.editprofile.EditProfileFragment

class InformationPersonFragment : TutorMeFragment<InformationPersonFragmentBinding>(R.layout.information_person_fragment) {

    override fun onInitView() {
        super.onInitView()
        binding.ivInformationPersonBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.ivInformationPersonEdit.setOnSafeClick {
            replaceFragment(EditProfileFragment())
        }

        binding.tvInformationPersonUserName.text = AppPreferences.userInfo?.fullName
        binding.tvInformationPersonGender.text = AppPreferences.userInfo?.getGenderUser()
        binding.tvInformationPersonDate.text = AppPreferences.userInfo?.date
        binding.tvInformationPersonPhone.text = "${AppPreferences.userInfo?.phoneNumber}"
        binding.tvInformationPersonAddress.text = AppPreferences.userInfo?.address
        if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
            binding.tvInformationPersonSchool.text = AppPreferences.userInfo?.nameSchool
        } else {
            binding.llInformationPersonSchool.gone()
        }
    }
}
