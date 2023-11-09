package vn.tutorme.mobile.presenter.main

import android.Manifest
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.stringee.StringeeClient
import com.stringee.call.StringeeCall
import com.stringee.call.StringeeCall2
import com.stringee.exception.StringeeError
import com.stringee.listener.StringeeConnectionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.CountNotifyEvent
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.databinding.MainActivityBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.chat.video.VideoCallInfo
import vn.tutorme.mobile.presenter.chat.videocall.MainVideoCallFragment
import vn.tutorme.mobile.presenter.chat.videocall.VideoCallFragment
import vn.tutorme.mobile.presenter.classmanager.ClassManagerFragment
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.notification.NotificationFragment
import vn.tutorme.mobile.presenter.profile.ProfileFragment
import vn.tutorme.mobile.presenter.splash.SplashFragment
import vn.tutorme.mobile.presenter.widget.bottombarview.SELECTED_STATE

@AndroidEntryPoint
class MainActivity : TutorMeActivity<MainActivityBinding>(R.layout.main_activity) {

    private val viewModel by viewModels<MainViewModel>()
    var strClient: StringeeClient? = null
    val callMap: HashMap<String, StringeeCall> = hashMapOf()

    override fun onInitView() {
        super.onInitView()
        setFragmentDefault()
        setBottomMainState()
        setOnMainClick()
        setBottomBarType()
        initVideoCall()
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
//                viewModel.getNotificationInfoList()
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

        coroutinesLaunch(viewModel.tokenVideoCallState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    viewModel.tokenVideoCall = it.data
                    strClient?.connect(it.data)
                    val ref = FirebaseDatabase.getInstance().getReference(HomeFragment.VIDEO_CALL_KEY)
                    if (ref.push().key != null) {
                        val videoCallInfo = VideoCallInfo(
                            AppPreferences.userInfo?.userId,
                            AppPreferences.userInfo?.fullName,
                            it.data,
                            System.currentTimeMillis() / 1000
                        )

                        ref.child(AppPreferences.userInfo?.userId!!)
                            .child(ref.push().key!!)
                            .setValue(videoCallInfo)
                    }
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

    private fun initVideoCall() {
        strClient = StringeeClient(this)
        strClient?.setConnectionListener(object : StringeeConnectionListener {
            override fun onConnectionConnected(p0: StringeeClient?, p1: Boolean) {}

            override fun onConnectionDisconnected(p0: StringeeClient?, p1: Boolean) {
//                TODO("Not yet implemented")
            }

            override fun onIncomingCall(p0: StringeeCall) {
                doRequestPermission(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ), object : PermissionListener {
                    override fun onAllow() {
                        runOnUiThread {
                            MainVideoCallFragment.callMap[p0.callId] = p0
                            replaceFragment(VideoCallFragment(), bundleOf(
                                VideoCallFragment.CALL_ID_KEY to p0.callId,
                                VideoCallFragment.STATE_CALL_KEY to 0
                            ))
                        }
                    }

                    override fun onDenied(neverAskAgainPermissionList: List<String>) {}
                })
            }

            override fun onIncomingCall2(p0: StringeeCall2?) {
//                TODO("Not yet implemented")
            }

            override fun onConnectionError(p0: StringeeClient?, p1: StringeeError?) {
                showError(getAppString(R.string.connect_error))
            }

            override fun onRequestNewToken(p0: StringeeClient?) {
//                TODO("Not yet implemented")
            }

            override fun onCustomMessage(p0: String?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }

            override fun onTopicMessage(p0: String?, p1: JSONObject?) {
//                TODO("Not yet implemented")
            }
        })

        strClient?.connect(viewModel.tokenVideoCall)
    }
}
