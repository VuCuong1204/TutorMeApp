package vn.tutorme.mobile.presenter.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.data
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.model.banner.mockDataBanner

class HomeViewModel(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {
    private val _homeState = MutableStateFlow(FlowResult.newInstance<DataPage<Any>>())
    val homeState = _homeState.asStateFlow()
    var homeDataList = DataPage.newInstance(_homeState.data(), true)

    init {
        getHomeData()
    }

    fun getHomeData(isReload: Boolean = true) {
        homeDataList = DataPage.newInstance(_homeState.data(), isReload)
        viewModelScope.launch {
            homeDataList.addData(mockDataBanner())
            _homeState.success(homeDataList)
        }
    }
}
