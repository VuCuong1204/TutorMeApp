package vn.tutorme.mobile.presenter.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
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
import vn.tutorme.mobile.base.common.InsertNotificationState
import vn.tutorme.mobile.base.common.NavigateLessonInfo
import vn.tutorme.mobile.base.common.SendVideoCallState
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.isLogin
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.databinding.MainActivityBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.chat.video.VideoCallInfo
import vn.tutorme.mobile.domain.model.notification.DeviceInfo
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.presenter.authen.login.LoginFragment
import vn.tutorme.mobile.presenter.chat.videocall.CALL_VIDEO_STATE
import vn.tutorme.mobile.presenter.chat.videocall.VideoCallFragment
import vn.tutorme.mobile.presenter.classmanager.ClassManagerFragment
import vn.tutorme.mobile.presenter.dialog.ConfirmVideoDialog
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.notification.NotificationFragment
import vn.tutorme.mobile.presenter.profile.ProfileFragment
import vn.tutorme.mobile.presenter.splash.SplashFragment
import vn.tutorme.mobile.presenter.widget.bottombarview.SELECTED_STATE
import vn.tutorme.mobile.utils.TimeUtils


@AndroidEntryPoint
class MainActivity : TutorMeActivity<MainActivityBinding>(R.layout.main_activity) {

    private val viewModel by viewModels<MainViewModel>()
    var strClient: StringeeClient? = null
    val callMap: HashMap<String, StringeeCall> = hashMapOf()
    private var confirmVideoDialog: ConfirmVideoDialog? = null
    private var callVideoType: Boolean = true

    override fun onInitView() {
        super.onInitView()
        setFragmentDefault()
        setBottomMainState()
        setOnMainClick()
        setBottomBarType()
        getDataIntent()
        getFcmToken()
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
                viewModel.getCountNotificationUnreadList()
                EventBusManager.instance?.removeSticky(event)
            }

            is SendVideoCallState -> {
                when (event.state) {
                    CALL_VIDEO_STATE.BUSY -> {
                        showError(getAppString(R.string.video_call_busy))
                    }

                    CALL_VIDEO_STATE.ENDED -> {
                        showSuccess(getAppString(R.string.video_call_end))
                    }

                    CALL_VIDEO_STATE.ERROR -> {
                        showError(getAppString(R.string.connect_error))
                    }
                }

                EventBusManager.instance?.removeSticky(event)
            }

            is InsertNotificationState -> {
                viewModel.insertNotification(
                    event.notificationInfo.title,
                    event.notificationInfo.content,
                    event.notificationInfo.notifyState,
                    event.notificationInfo.notifyType,
                    event.notificationInfo.timeSend,
                    event.notificationInfo.userId,
                    event.notificationInfo.refInfo?.lessonId,
                    event.notificationInfo.refInfo?.classId,
                )
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
            }, canShowError = false)
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
            }, canShowError = false)
        }

        coroutinesLaunch(viewModel.insertNotificationState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    viewModel.getCountNotificationUnreadList()
                }
            }, canShowError = false)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        replaceFragment(
            fragment = HomeFragment(),
            screenAnim = SlideAnimation(),
            bundle = null
        )
        lifecycleScope.launch {
            delay(1000L)
            getDataIntent(intentSingleTask = intent, insertNotifyState = false)
        }
    }

    override fun hideLoading() {
        binding.icMainLoading.gone()
    }

    private fun getDataIntent(intentSingleTask: Intent? = null, insertNotifyState: Boolean = true) {
        val intent = intentSingleTask ?: intent
        val title = intent?.getStringExtra("title")
        val body = intent?.getStringExtra("body")
        val lessonId = intent?.getStringExtra("lessonId")
        val classId = intent?.getStringExtra("classId")
        val notificationType = NOTIFICATION_TYPE.valueOfName(intent?.getStringExtra("notificationType")?.toInt())
        Log.d("TAG", "getDataIntent: $title $body $lessonId $classId")
        if (isLogin() && title != null
            && body != null
            && lessonId != null
            && classId != null
        ) {
            if (insertNotifyState) {
                viewModel.insertNotification(
                    title = title,
                    content = body,
                    notifyState = NOTIFICATION_STATE.READ_STATE,
                    notifyType = notificationType,
                    timeSend = TimeUtils.getTimeCurrent(),
                    userId = AppPreferences.userInfo?.userId,
                    lessonId = lessonId.toInt(),
                    classId = classId
                )
            }

            EventBusManager.instance?.postPending(NavigateLessonInfo(lessonId.toInt(), classId))
        }
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

    fun initVideoCall() {
        strClient = StringeeClient(this)
        strClient?.setConnectionListener(object : StringeeConnectionListener {
            override fun onConnectionConnected(p0: StringeeClient?, p1: Boolean) {}

            override fun onConnectionDisconnected(p0: StringeeClient?, p1: Boolean) {
                callVideoType = true
                confirmVideoDialog?.dismiss()
            }

            override fun onIncomingCall(p0: StringeeCall) {
                doRequestPermission(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ), object : PermissionListener {
                    override fun onAllow() {
                        runOnUiThread {
                            if (callVideoType) {
                                showIncomingVideoDialog(p0)
                                callVideoType = false
                            }
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

        viewModel.tokenVideoCall?.let { strClient?.connect(viewModel.tokenVideoCall) }
    }

    fun showIncomingVideoDialog(strCall: StringeeCall) {
        var videoCallInfo: VideoCallInfo? = VideoCallInfo()
        FirebaseDatabase.getInstance().getReference(HomeFragment.VIDEO_CALL_KEY).child(strCall.from)
            .get()
            .addOnSuccessListener {
                it.children.forEach { dataSnapShot ->
                    videoCallInfo = dataSnapShot.getValue(VideoCallInfo::class.java)
                }
            }

        confirmVideoDialog = ConfirmVideoDialog().apply {
            userName = videoCallInfo?.userName
            listener = object : ConfirmVideoDialog.IConfirmVideoListener {
                override fun onRejectClick() {
                    callVideoType = true
                    strCall.reject(null)
                }

                override fun onConfirmClick() {
                    callVideoType = true
                    callMap[strCall.callId] = strCall
                    replaceFragment(VideoCallFragment(), bundleOf(
                        VideoCallFragment.CALL_ID_KEY to strCall.callId,
                        VideoCallFragment.STATE_CALL_KEY to 0
                    ))
                }
            }
        }
        confirmVideoDialog?.show(supportFragmentManager, ConfirmVideoDialog::class.simpleName)
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "getFcmToken: ${task.result}")
                AppPreferences.deviceId = task.result
            }
        }
    }

    fun registerDeviceForNotification() {
        val deviceId: String? = AppPreferences.deviceId
        val userId: String? = AppPreferences.userInfo?.userId
        if (deviceId != null && userId != null) {
            FirebaseDatabase.getInstance()
                .getReference(LoginFragment.TOKEN_DEVICE_NOTIFICATION)
                .child(deviceId)
                .removeValue()

            FirebaseDatabase.getInstance()
                .getReference(LoginFragment.TOKEN_DEVICE_NOTIFICATION)
                .child(deviceId)
                .setValue(
                    DeviceInfo(deviceId, userId)
                )
        }
    }

    fun unregisterDeviceForNotification() {
        val deviceId: String? = AppPreferences.deviceId
        val userId: String? = AppPreferences.userInfo?.userId
        if (deviceId != null && userId != null) {
            FirebaseDatabase.getInstance()
                .getReference(LoginFragment.TOKEN_DEVICE_NOTIFICATION)
                .child(deviceId)
                .removeValue()
        }
    }
}
