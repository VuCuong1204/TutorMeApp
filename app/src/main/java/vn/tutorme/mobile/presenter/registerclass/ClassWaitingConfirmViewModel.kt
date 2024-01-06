package vn.tutorme.mobile.presenter.registerclass

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
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.reset
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.usecase.GetClassTeacherRegisterUseCase
import vn.tutorme.mobile.domain.usecase.UpdateStateClassRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class ClassWaitingConfirmViewModel @Inject constructor(
    private val getClassTeacherRegisterUseCase: GetClassTeacherRegisterUseCase,
    private val updateStateClassRegisterUseCase: UpdateStateClassRegisterUseCase
) : BaseViewModel() {
    companion object {
        const val TIME_DELAY = 200L
    }

    private val _classInfoState = MutableStateFlow(FlowResult.newInstance<DataPage<ClassInfo>>())
    val classInfoState = _classInfoState.asStateFlow()
    private var classDataPage = DataPage.newInstance(_classInfoState.value.data, true)

    init {
        getClassInfoList(true, 0)
    }

    fun getClassInfoList(isReload: Boolean = false, state: Int) {
        viewModelScope.launch {
            delay(TIME_DELAY)
            classDataPage = DataPage.newInstance(_classInfoState.value.data, true)
            val rv = GetClassTeacherRegisterUseCase.GetClassTeacherRegisterRV(
                0L,
                state,
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT
            )

            getClassTeacherRegisterUseCase.invoke(rv)
                .onStart {
                    _classInfoState.loading()
                }
                .onException {
                    _classInfoState.failure(it)
                }
                .collect {
                    classDataPage.replaceDataList(it)
                    _classInfoState.success(classDataPage)
                }
        }
    }

    fun updateClassRegister(isReload: Boolean = false, state: Int, classId: String) {
        viewModelScope.launch {
            classDataPage = DataPage.newInstance(_classInfoState.value.data, true)
            val rv = UpdateStateClassRegisterUseCase.UpdateStateClassRegisterRV(
                classId,
                state,
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT
            )

            updateStateClassRegisterUseCase.invoke(rv)
                .onStart {
                    _classInfoState.loading()
                }
                .onException {
                    _classInfoState.failure(it)
                }
                .collect {
                    classDataPage.replaceDataList(it)
                    _classInfoState.success(classDataPage)
                }
        }
    }

    fun reset(){
        _classInfoState.reset()
    }
}
