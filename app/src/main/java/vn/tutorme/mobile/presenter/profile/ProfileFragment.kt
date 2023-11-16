package vn.tutorme.mobile.presenter.profile

import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ProfileFragmentBinding
import vn.tutorme.mobile.domain.model.profile.PROFILE_TYPE
import vn.tutorme.mobile.domain.model.profile.ProfileInfo
import vn.tutorme.mobile.domain.model.profile.mockDataProfileInfo
import vn.tutorme.mobile.presenter.authen.login.LoginFragment
import vn.tutorme.mobile.presenter.bannerinfo.course.CourseFragment
import vn.tutorme.mobile.presenter.bannerinfo.event.EventDetailFragment
import vn.tutorme.mobile.presenter.chat.ChatFragment
import vn.tutorme.mobile.presenter.chat.user.ChatUserFragment
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.lessondetail.LessonDetailFragment
import vn.tutorme.mobile.presenter.ratestudent.RateStudentFragment

@AndroidEntryPoint
class ProfileFragment : TutorMeFragment<ProfileFragmentBinding>(R.layout.profile_fragment) {

    private val profileAdapter by lazy { ProfileAdapter() }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addListener()
        addAdapter()
    }

    private fun addHeader() {
        binding.tvProfileName.text = AppPreferences.userInfo?.fullName
        binding.ivProfileUser.loadUser(AppPreferences.userInfo?.avatar)
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }

    private fun addListener() {
        profileAdapter.listener = object : IProfileListener {
            override fun onItemClick(item: ProfileInfo) {
                when (item.type) {
                    PROFILE_TYPE.INFO_TYPE -> {
                        replaceFragment(LessonDetailFragment())
                    }

                    PROFILE_TYPE.INFO_CONTACT_TYPE -> {
                        replaceFragment(ChatFragment())
                        replaceFragment(EventDetailFragment())
                    }

                    PROFILE_TYPE.CHANGE_PASSWORD_TYPE -> {
                        replaceFragment(RateStudentFragment())
                    }

                    PROFILE_TYPE.CHAT_TYPE -> {
                        replaceFragment(ChatUserFragment())
                        replaceFragment(CourseFragment())
                    }

                    PROFILE_TYPE.LOGOUT_TYPE -> {
                        showLogoutDialog()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun addAdapter() {
        binding.cvProfileContent.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.GRIDLAYOUT_VERTICAL, 3)
            setBaseAdapter(profileAdapter)
            submitList(mockDataProfileInfo())
        }
    }

    private fun removeListener() {
        profileAdapter.listener = null
    }

    private fun showLogoutDialog() {
        BottomSheetConfirmDialog().apply {
            title = getAppString(R.string.log_out_second)
            content = getAppString(R.string.logout_notify)
            textLeft = getAppString(R.string.confirm)
            avatar = getAppDrawable(R.drawable.ic_logout)
            textRight = getAppString(R.string.cancel)
            bgTextLeft = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
            bgTextRight = getAppDrawable(R.drawable.ripple_bg_white_corner_16_stroke_1)
            clTextLeft = getAppColor(R.color.white)
            clTextRight = getAppColor(R.color.text1)
            eventLeftClick {
                AppPreferences.userInfo = null
                mainViewModel.indexFragmentInBackStack = 0
                Firebase.auth.signOut()
                clearBackStackFragment()
                replaceFragment(fragment = LoginFragment(),
                    keepToBackStack = false,
                    screenAnim = SlideAnimation()
                )
            }
        }.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }
}
