package vn.tutorme.mobile.presenter.profile.contact

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ContactFragmentBinding

class ContactFragment : TutorMeFragment<ContactFragmentBinding>(R.layout.contact_fragment) {

    override fun onInitView() {
        super.onInitView()
        binding.ivContactBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }
}
