package vn.tutorme.mobile.presenter.bannerinfo.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo
import vn.tutorme.mobile.domain.usecase.course.GetBannerInfoEventUseCase
import vn.tutorme.mobile.presenter.bannerinfo.event.EventDetailFragment.Companion.EVENT_ID_KEY
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getBannerInfoEventUseCase: GetBannerInfoEventUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _eventInfoState = MutableStateFlow(FlowResult.newInstance<BannerEventInfo>())
    val eventInfoState = _eventInfoState

    private var eventId = savedStateHandle.get<Int>(EVENT_ID_KEY) ?: 7

    init {
        getEventInfo()
    }

    fun getEventInfo() {
        viewModelScope.launch {
            val rv = GetBannerInfoEventUseCase.GetBannerInfoEventRV(eventId)
            getBannerInfoEventUseCase.invoke(rv)
                .onStart {
                    _eventInfoState.loading()
                }
                .onException {
//                    _eventInfoState.failure(it)
                }
                .collect {
                    _eventInfoState.success(it)
                }
        }
    }
}
