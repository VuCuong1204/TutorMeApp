package vn.tutorme.mobile.presenter.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.usecase.notification.GetNotificationCountUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNotificationCountUseCase: GetNotificationCountUseCase
) : BaseViewModel() {

    private val _notificationState = MutableStateFlow(FlowResult.newInstance<Int>())
    val notificationState = _notificationState.asStateFlow()

    var indexFragmentInBackStack: Int = 0

    fun getNotificationInfoList() {
        viewModelScope.launch {
            val rv = GetNotificationCountUseCase.GetNotificationCountRV("vucuonghihi")
            getNotificationCountUseCase.invoke(rv)
                .collect {
                    _notificationState.success(it)
                }
        }
    }
}
