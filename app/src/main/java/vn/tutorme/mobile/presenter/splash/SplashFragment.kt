package vn.tutorme.mobile.presenter.splash

import android.text.style.ForegroundColorSpan
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.anim.FadeAnim
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.isLogin
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.SplashFragmentBinding
import vn.tutorme.mobile.presenter.authen.login.LoginFragment
import vn.tutorme.mobile.presenter.home.HomeFragment

class SplashFragment : TutorMeFragment<SplashFragmentBinding>(R.layout.splash_fragment) {

    override fun onInitView() {
        super.onInitView()

        val spannedText = SpannableBuilder()
            .appendText(getAppString(R.string.title_first))
            .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
            .appendText(" " + getAppString(R.string.title_second))
            .withSpan(ForegroundColorSpan(getAppColor(R.color.secondary)))
            .spannedText

        binding.tvSplashTitle.text = spannedText

        lifecycleScope.launch {
            delay(3000)
            checkLogin()
        }
    }

    private fun checkLogin() {
        if (isLogin()) {
            replaceFragment(
                fragment = HomeFragment(),
                bundle = bundleOf(
                    HomeFragment.USER_ID_KEY to AppPreferences.userInfo?.userId
                ),
                screenAnim = FadeAnim()
            )
        } else {
            replaceFragment(
                fragment = LoginFragment(),
                screenAnim = FadeAnim()
            )
        }
    }
}
