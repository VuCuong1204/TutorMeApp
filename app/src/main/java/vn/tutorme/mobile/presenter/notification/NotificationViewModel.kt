package vn.tutorme.mobile.presenter.notification

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.reset
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.usecase.GetNotificationInfoList
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationInfoList: GetNotificationInfoList
) : BaseViewModel() {
    private val _notificationState = MutableStateFlow(FlowResult.newInstance<DataPage<NotificationInfo>>())
    val notificationState = _notificationState.asStateFlow()
    var notificationDataPage = DataPage.newInstance(_notificationState.value.data, true)

    init {
        getNotificationInfoList(true)
    }

    fun getNotificationInfoList(isReload: Boolean = true, isShowLoading: Boolean = true) {
        viewModelScope.launch {
            notificationDataPage = DataPage.newInstance(_notificationState.value.data, isReload)
            getNotificationInfoList.invoke(BaseUseCase.VoidRequest())
                .onStart {
                    if (isShowLoading) {
                        _notificationState.loading()
                    }
                }
                .onException {
                    _notificationState.failure(it)
                }
                .collect {
                    delay(1000)
                    val newList = it.toMutableList()
                    if (isReload) {
                        notificationDataPage.clearDataPage()
                    } else {
                        if (notificationDataPage.dataList.isNotEmpty()) {
                            val list = notificationDataPage.dataList.toMutableList()
                            val newItem = list[list.lastIndex]
                            list[list.lastIndex] = newItem.copy(isLastIndex = true)
                            notificationDataPage.replaceDataList(list)
                        }
                    }

                    notificationDataPage.addAllDataList(newList)
                    _notificationState.success(notificationDataPage)
                }
        }
    }

    fun resetNotificationState() {
        _notificationState.reset()
    }
}
