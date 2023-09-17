package vn.tutorme.mobile.presenter.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.usecase.GetUserInfoUseCase
import vn.tutorme.mobile.domain.usecase.GetUserInfoUseCase1
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserInfoUseCase1: GetUserInfoUseCase1
) : BaseViewModel() {
    private val _userState = MutableStateFlow(FlowResult.newInstance<String>())
    val userState = _userState.asStateFlow()

    private val _userState1 = MutableStateFlow(FlowResult.newInstance<String>())
    val userState1 = _userState1.asStateFlow()

    init {
        getUserInfo()
        getUserInfo1()
    }

    fun getUserInfo() {
        viewModelScope.launch {
            getUserInfoUseCase.invoke(BaseUseCase.VoidRequest()).collect {
                _userState.success(it)
            }
        }
    }

    fun getUserInfo1() {
        viewModelScope.launch {
            getUserInfoUseCase1.invoke(BaseUseCase.VoidRequest()).collect {
                _userState.success(it)
            }
        }
    }
}
