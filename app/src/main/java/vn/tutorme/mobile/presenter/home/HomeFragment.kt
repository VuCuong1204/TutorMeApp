package vn.tutorme.mobile.presenter.home

import androidx.fragment.app.viewModels
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.HomeFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE

class HomeFragment : TutorMeFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    companion object {
        const val USER_ID_KEY = "USER_ID_KEY"
    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onInitView() {
        super.onInitView()

        //  toast(viewModel.id)
    }
}
