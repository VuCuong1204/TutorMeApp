package vn.tutorme.mobile.presenter.classall

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
import vn.tutorme.mobile.domain.usecase.GetClassAllStudentUseCase
import javax.inject.Inject

@HiltViewModel
class ClassAllViewModel @Inject constructor(
    private val getClassAllStudentUseCase: GetClassAllStudentUseCase
) : BaseViewModel() {

    companion object {
        const val TIME_DELAY = 100L
    }

    private val _classInfoState = MutableStateFlow(FlowResult.newInstance<DataPage<ClassInfo>>())
    val classInfoState = _classInfoState.asStateFlow()
    private var classInfoDataPage = DataPage.newInstance(_classInfoState.value.data, true)

    init {
        getClassInfo()
    }

    fun getClassInfo(isReload: Boolean = false) {
        viewModelScope.launch {
            delay(TIME_DELAY)
            classInfoDataPage = DataPage.newInstance(_classInfoState.value.data, isReload)
            val rv = GetClassAllStudentUseCase.GetClassAllStudentVH(
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT
            )
            getClassAllStudentUseCase.invoke(rv)
                .onStart {
                    if (!isReload) {
                        _classInfoState.loading()
                    }
                }
                .onException {
                    _classInfoState.failure(it)
                }
                .collect {
                    classInfoDataPage.addAllDataList(it)
                    _classInfoState.success(classInfoDataPage)
                }
        }
    }

    fun reset() {
        _classInfoState.reset()
    }
}
