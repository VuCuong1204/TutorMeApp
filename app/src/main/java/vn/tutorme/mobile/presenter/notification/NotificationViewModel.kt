package vn.tutorme.mobile.presenter.notification

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.data
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.reset
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.usecase.notification.DeleteNotificationUseCase
import vn.tutorme.mobile.domain.usecase.notification.GetNotificationListUseCase
import vn.tutorme.mobile.domain.usecase.notification.UpdateNotificationAllUseCase
import vn.tutorme.mobile.domain.usecase.notification.UpdateNotificationUseCase
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationListUseCase: GetNotificationListUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
    private val updateNotificationAllUseCase: UpdateNotificationAllUseCase
) : BaseViewModel() {

    companion object {
        const val TIME_DELAY = 1000L
    }

    private val _notificationState = MutableStateFlow(FlowResult.newInstance<DataPage<NotificationInfo>>())
    val notificationState = _notificationState.asStateFlow()
    private var notificationDataPage = DataPage.newInstance(_notificationState.data(), true)

    init {
        getNotificationInfoList(true)
    }

    fun getNotificationInfoList(isReload: Boolean = true, isShowLoading: Boolean = true) {
        viewModelScope.launch {
            notificationDataPage = DataPage.newInstance(_notificationState.data(), isReload)
            val rv = GetNotificationListUseCase.GetNotificationListRV(
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT
            ).apply {
                page = notificationDataPage.page * notificationDataPage.limitPage
                size = notificationDataPage.limitPage
            }
            getNotificationListUseCase.invoke(rv)
                .onStart {
                    if (isShowLoading) {
                        _notificationState.loading()
                    }
                }
                .onException {
                    _notificationState.failure(it)
                }
                .collect {
                    delay(TIME_DELAY)
                    if (isReload) {
                        notificationDataPage.clearDataPage()
                    }

                    notificationDataPage.replaceDataList(it)
                    _notificationState.success(notificationDataPage)
                }
        }
    }

    fun addNotification() {
        viewModelScope.launch {}
    }

    fun removeNotification(id: Int) {
        viewModelScope.launch {

            val rv = DeleteNotificationUseCase.DeleteNotificationRV(id)
            deleteNotificationUseCase.invoke(rv)
                .onException {
                    _notificationState.failure(it)
                }
                .collect {
                    val list = notificationDataPage.dataList.toMutableList()

                    val oldIndex = list.indexOfFirst {
                        it.id == id
                    }

                    if (oldIndex in 0..list.lastIndex) {
                        list.removeAt(oldIndex)
                    }

                    notificationDataPage.replaceDataList(list)
                    _notificationState.success(notificationDataPage)
                }

        }
    }

    fun changeReadState(id: Int) {
        viewModelScope.launch {

            val rv = UpdateNotificationUseCase.UpdateNotificationRV(id)
            updateNotificationUseCase.invoke(rv)
                .onException {
                    _notificationState.failure(it)
                }
                .collect {
                    val list = notificationDataPage.dataList.toMutableList()
                    val oldIndex = list.indexOfFirst {
                        it.id == id
                    }

                    if (oldIndex in 0..list.lastIndex) {
                        val newItem = list[oldIndex].copy(
                            notifyState = NOTIFICATION_STATE.READ_STATE
                        )

                        list[oldIndex] = newItem
                    }

                    notificationDataPage.replaceDataList(list)
                    _notificationState.success(notificationDataPage)
                }
        }
    }

    fun readAllNotification() {
        viewModelScope.launch {

            val rv = UpdateNotificationAllUseCase.UpdateNotificationAllRV(
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT
            )
            updateNotificationAllUseCase.invoke(rv)
                .onException {
                    _notificationState.failure(it)
                }
                .collect {

                    val list = notificationDataPage.dataList.toMutableList()

                    val newList = mutableListOf<NotificationInfo>()
                    list.map {
                        newList.add(it.copy(
                            notifyState = NOTIFICATION_STATE.READ_STATE
                        ))
                    }

                    notificationDataPage.replaceDataList(newList)
                    _notificationState.success(notificationDataPage)
                }
        }
    }

    fun resetNotificationState() {
        _notificationState.reset()
    }
}
