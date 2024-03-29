package vn.tutorme.mobile.presenter.authen.register

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
import vn.tutorme.mobile.domain.usecase.GetUserInfoRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getUserInfoRegisterUseCase: GetUserInfoRegisterUseCase
) : BaseViewModel() {
    private val _userInfoState = MutableStateFlow(FlowResult.newInstance<UserInfo>())
    val userInfoState = _userInfoState.asStateFlow()

    fun register(id: String, email: String, password: String) {
        viewModelScope.launch {
            val rv = GetUserInfoRegisterUseCase.GetUserInfoRegisterRV(id).apply {
                this.email = email
                this.password = password
            }
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
