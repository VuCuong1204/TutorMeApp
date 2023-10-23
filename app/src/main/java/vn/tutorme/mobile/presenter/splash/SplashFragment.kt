package vn.tutorme.mobile.presenter.splash

import android.text.style.ForegroundColorSpan
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.CountNotifyEvent
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.isLogin
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.SplashFragmentBinding
import vn.tutorme.mobile.presenter.authen.login.LoginFragment
import vn.tutorme.mobile.presenter.home.HomeFragment

class SplashFragment : TutorMeFragment<SplashFragmentBinding>(R.layout.splash_fragment) {

    private val auth = FirebaseAuth.getInstance()

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
            loginFirebase()
        } else {
            replaceFragment(
                fragment = LoginFragment(),
                keepToBackStack = false,
                screenAnim = SlideAnimation()
            )
        }
    }

    private fun loginFirebase() {
        if (AppPreferences.userNameAccount != "" && AppPreferences.passwordAccount != "") {
            auth.signInWithEmailAndPassword(AppPreferences.userNameAccount!!, AppPreferences.passwordAccount!!)
                .addOnCompleteListener(mainActivity) { task ->
                    if (task.isSuccessful) {
                        EventBusManager.instance?.postPending(CountNotifyEvent())
                        replaceFragment(
                            fragment = HomeFragment(),
                            screenAnim = SlideAnimation()
                        )
                    } else {
                        showError(getAppString(R.string.login_failed))
                        replaceFragment(
                            fragment = LoginFragment(),
                            keepToBackStack = false,
                            screenAnim = SlideAnimation()
                        )
                    }
                }
        } else {
            showError(getAppString(R.string.login_failed))
            replaceFragment(
                fragment = LoginFragment(),
                keepToBackStack = false,
                screenAnim = SlideAnimation()
            )
        }
    }
}
