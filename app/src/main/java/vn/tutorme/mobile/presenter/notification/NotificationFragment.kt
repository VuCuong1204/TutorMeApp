package vn.tutorme.mobile.presenter.notification

import androidx.fragment.app.activityViewModels
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.NotificationFragmentBinding
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.main.MainViewModel

class NotificationFragment : TutorMeFragment<NotificationFragmentBinding>(R.layout.notification_fragment) {

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onInitView() {
        super.onInitView()
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }
}
