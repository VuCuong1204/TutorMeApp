package vn.tutorme.mobile.presenter.splash

import android.text.style.ForegroundColorSpan
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.SplashFragmentBinding

class SplashFragment : TutorMeFragment<SplashFragmentBinding>(R.layout.splash_fragment) {
    override fun onInitView() {
        super.onInitView()

        val spannedText = SpannableBuilder()
            .appendText(getAppString(R.string.title_first))
            .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
            .appendText(" " + getAppString(R.string.title_second))
            .withSpan(ForegroundColorSpan(getAppColor(R.color.yellow)))
            .spannedText

        binding.tvSplashTitle.text = spannedText
    }
}
