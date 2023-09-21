package vn.tutorme.mobile.presenter.authen.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.usecase.GetUserInfoLoginUseCase
import vn.tutorme.mobile.domain.usecase.GetUserInfoRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserInfoLoginUseCase: GetUserInfoLoginUseCase,
    private val getUserInfoRegisterUseCase: GetUserInfoRegisterUseCase
) : BaseViewModel() {
    private val _userInfoState = MutableStateFlow(FlowResult.newInstance<UserInfo>())
    val userInfoState = _userInfoState.asStateFlow()

    fun login(id: String) {
        viewModelScope.launch {
            val rv = GetUserInfoLoginUseCase.GetUserInfoLoginRV(id)
            getUserInfoLoginUseCase.invoke(rv)
                .onStart {
                    _userInfoState.loading()
                }
                .onException {
                    _userInfoState.failure(it)
                }
                .collect {
                    _userInfoState.success(it)
                }
        }
    }

    fun register(id: String) {
        viewModelScope.launch {
            val rv = GetUserInfoRegisterUseCase.GetUserInfoRegisterRV(id)
            getUserInfoRegisterUseCase.invoke(rv)
                .onStart {
                    _userInfoState.loading()
                }
                .onException {
                    _userInfoState.failure(it)
                }
                .collect {
                    _userInfoState.success(it)
                }
        }
    }
}
