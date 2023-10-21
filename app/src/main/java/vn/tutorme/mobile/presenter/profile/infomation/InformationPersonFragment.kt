package vn.tutorme.mobile.presenter.profile.infomation

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.InformationPersonFragmentBinding

class InformationPersonFragment : TutorMeFragment<InformationPersonFragmentBinding>(R.layout.information_person_fragment) {

    override fun onInitView() {
        super.onInitView()
        binding.ivInformationPersonBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.tvInformationPersonUserName.text = AppPreferences.userInfo?.fullName
        binding.tvInformationPersonDate.text = AppPreferences.userInfo?.date
        binding.tvInformationPersonPhone.text = "${AppPreferences.userInfo?.phoneNumber}"
        binding.tvInformationPersonAddress.text = AppPreferences.userInfo?.address
        binding.tvInformationPersonSchool.text = AppPreferences.userInfo?.nameSchool
    }
}
