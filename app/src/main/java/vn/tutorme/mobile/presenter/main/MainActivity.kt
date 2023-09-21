package vn.tutorme.mobile.presenter.main

import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.databinding.MainActivityBinding
import vn.tutorme.mobile.presenter.authen.login.LoginFragment
import vn.tutorme.mobile.presenter.splash.SplashFragment

@AndroidEntryPoint
class MainActivity : TutorMeActivity<MainActivityBinding>(R.layout.main_activity) {

    override fun onInitView() {
        super.onInitView()

        replaceFragment(SplashFragment(), null, false)
    }

    override fun getContainerId(): Int = R.id.flMainRoot

    override fun showLoading() {
        binding.icMainLoading.show()
    }

    override fun hideLoading() {
        binding.icMainLoading.gone()
    }
}
