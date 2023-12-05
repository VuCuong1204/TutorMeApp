package vn.tutorme.mobile.presenter.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.domain.usecase.notification.GetNotificationCountUseCase
import vn.tutorme.mobile.domain.usecase.notification.InsertNotificationUseCase
import vn.tutorme.mobile.domain.usecase.notification.UpdateNotificationUseCase
import vn.tutorme.mobile.domain.usecase.stringee.GetAccessTokenVideoUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNotificationCountUseCase: GetNotificationCountUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
    private val getAccessTokenVideoUseCase: GetAccessTokenVideoUseCase,
    private val insertNotificationUseCase: InsertNotificationUseCase
) : BaseViewModel() {

    private val _tokenVideoCallState = MutableStateFlow(FlowResult.newInstance<String>())
    val tokenVideoCallState = _tokenVideoCallState.asStateFlow()
    var tokenVideoCall: String? = null

    private val _notificationState = MutableStateFlow(FlowResult.newInstance<Int>())
    val notificationState = _notificationState.asStateFlow()

    private val _updateNotificationState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val updateNotificationState = _updateNotificationState.asStateFlow()

    private val _insertNotificationState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val insertNotificationState = _insertNotificationState.asStateFlow()

    var indexFragmentInBackStack: Int = 0

    init {
        getCountNotificationUnreadList()
    }

    fun getCountNotificationUnreadList() {
        viewModelScope.launch {
            val rv = GetNotificationCountUseCase.GetNotificationCountRV(AppPreferences.userInfo?.userId
                ?: STRING_DEFAULT)
            getNotificationCountUseCase.invoke(rv)
                .onException {
                    _notificationState.failure(it)
                }
                .collect {
                    _notificationState.success(it)
                }
        }
    }

    fun insertNotification(
        title: String?,
        content: String?,
        notifyState: NOTIFICATION_STATE?,
        notifyType: NOTIFICATION_TYPE?,
        timeSend: Long?,
        userId: String?,
        lessonId: Int?,
        classId: String?
    ) {
        viewModelScope.launch {
            val rv = InsertNotificationUseCase.InsertNotificationRV(
                userId ?: STRING_DEFAULT
            ).apply {
                this.title = title ?: STRING_DEFAULT
                this.content = content ?: STRING_DEFAULT
                this.notifyState = notifyState ?: NOTIFICATION_STATE.UNREAD_STATE
                this.notifyType = notifyType ?: NOTIFICATION_TYPE.PREPARE_STUDY_TYPE
                this.timeSend = timeSend ?: LONG_DEFAULT
                this.lessonId = lessonId ?: INT_DEFAULT
                this.classId = classId ?: STRING_DEFAULT
            }

            insertNotificationUseCase.invoke(rv)
                .onException {
                    _insertNotificationState.failure(it)
                }
                .collect {
                    _insertNotificationState.success(it)
                }
        }
    }

    fun getAccessTokenVideo(userId: String) {
        viewModelScope.launch {
            val rv = GetAccessTokenVideoUseCase.GetAccessTokenVideoRV(userId)
            getAccessTokenVideoUseCase.invoke(rv)
                .onException {
                    _tokenVideoCallState.failure(it)
                }
                .collect {
                    _tokenVideoCallState.success(it)
                }
        }
    }
}
