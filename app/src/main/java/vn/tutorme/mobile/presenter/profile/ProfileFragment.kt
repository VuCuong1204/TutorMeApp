package vn.tutorme.mobile.presenter.profile

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ProfileFragmentBinding
import vn.tutorme.mobile.presenter.authen.login.LoginFragment
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.main.MainViewModel

class ProfileFragment : TutorMeFragment<ProfileFragmentBinding>(R.layout.profile_fragment) {

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onInitView() {
        super.onInitView()

        binding.btnProfileLogout.setOnSafeClick {
            AppPreferences.userInfo = null
            mainViewModel.indexFragmentInBackStack = 0
            Firebase.auth.signOut()
            clearBackStackFragment()
            replaceFragment(fragment = LoginFragment(), keepToBackStack = false, screenAnim = SlideAnimation())
        }
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }
}
