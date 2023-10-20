package vn.tutorme.mobile.presenter.main

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.CountNotifyEvent
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.databinding.MainActivityBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.presenter.classmanager.ClassManagerFragment
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.notification.NotificationFragment
import vn.tutorme.mobile.presenter.profile.ProfileFragment
import vn.tutorme.mobile.presenter.splash.SplashFragment
import vn.tutorme.mobile.presenter.widget.bottombarview.SELECTED_STATE

@AndroidEntryPoint
class MainActivity : TutorMeActivity<MainActivityBinding>(R.layout.main_activity) {

    private val viewModel by viewModels<MainViewModel>()

    override fun onInitView() {
        super.onInitView()
        setFragmentDefault()
        setBottomMainState()
        setOnMainClick()
        setBottomBarType()
    }

    override fun getContainerId(): Int = R.id.flMainRoot

    override fun showLoading() {
        binding.icMainLoading.show()
    }

    override fun onStart() {
        super.onStart()
        EventBusManager.instance?.register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBusManager.instance?.unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: IEvent) {
        super.onEvent(event)
        when (event) {
            is CountNotifyEvent -> {
                viewModel.getNotificationInfoList()
                EventBusManager.instance?.removeSticky(event)
            }
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.notificationState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    it.data?.let { count -> binding.bmvMainTab.setCountNotification(count) }
                }
            })
        }
    }

    override fun hideLoading() {
        binding.icMainLoading.gone()
    }

    private fun setFragmentDefault() {
        replaceFragment(SplashFragment(), null, false)
    }

    private fun setOnMainClick() {
        binding.bmvMainTab.setOnTabClick {
            when (it) {
                SELECTED_STATE.HOME -> {
                    replaceFragmentInitialState(HomeFragment(), viewModel.indexFragmentInBackStack)
                }

                SELECTED_STATE.CLASS -> {
                    replaceFragmentInitialState(ClassManagerFragment(), viewModel.indexFragmentInBackStack)
                    if (supportFragmentManager.findFragmentByTag(getTag(ClassManagerFragment())) == null) viewModel.indexFragmentInBackStack++
                }

                SELECTED_STATE.NOTIFICATION -> {
                    replaceFragmentInitialState(NotificationFragment(), viewModel.indexFragmentInBackStack)
                    if (supportFragmentManager.findFragmentByTag(getTag(ClassManagerFragment())) == null) viewModel.indexFragmentInBackStack++
                }

                SELECTED_STATE.PROFILE -> {
                    replaceFragmentInitialState(ProfileFragment(), viewModel.indexFragmentInBackStack)
                    if (supportFragmentManager.findFragmentByTag(getTag(ClassManagerFragment())) == null) viewModel.indexFragmentInBackStack++
                }
            }
        }
    }

    private fun getTag(fragment: Fragment): String {
        return fragment::class.java.simpleName
    }

    private fun setBottomMainState() {
        val fragmentManager = supportFragmentManager
        fragmentManager.addOnBackStackChangedListener {
            lifecycleScope.launch {
                delay(100)
                val currentFragment = fragmentManager.findFragmentById(R.id.flMainRoot)
                    ?: Fragment()

                val currentState = when (getTag(currentFragment)) {
                    getTag(HomeFragment()) -> SELECTED_STATE.HOME
                    getTag(ClassManagerFragment()) -> SELECTED_STATE.CLASS
                    getTag(NotificationFragment()) -> SELECTED_STATE.NOTIFICATION
                    getTag(ProfileFragment()) -> SELECTED_STATE.PROFILE
                    else -> null
                }

                if (currentState != null) {
                    binding.bmvMainTab.changeSelectedState(currentState)
                    if (currentState != SELECTED_STATE.NOTIFICATION) {
                        binding.bmvMainTab.showNotifyCountState(false)
                    }
                    binding.bmvMainTab.show()
                    binding.vMain.show()
                } else {
                    binding.bmvMainTab.gone()
                    binding.vMain.gone()
                }
            }
        }
    }

    fun setBottomBarType() {
        binding.bmvMainTab.setBottomBarType(AppPreferences.userInfo?.role ?: ROLE_TYPE.STUDENT_TYPE)
    }
}
