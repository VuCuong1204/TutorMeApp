package vn.tutorme.mobile.presenter.classmanager

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.reset
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.usecase.GetClassTeacherListUseCase
import javax.inject.Inject

@HiltViewModel
class ClassManagerViewModel @Inject constructor(
    private val getClassTeacherListUseCase: GetClassTeacherListUseCase
) : BaseViewModel() {

    private val _classInfoState = MutableStateFlow(FlowResult.newInstance<DataPage<ClassInfo>>())
    val classInfoState = _classInfoState.asStateFlow()
    var classInfoDataPage = DataPage.newInstance(_classInfoState.value.data, true)

    var classType = CLASS_TYPE.DEMO_TYPE

    init {
        getClassList()
    }

    fun getClassList(isReload: Boolean = false, isLoadMore: Boolean = false) {
        viewModelScope.launch {
            classInfoDataPage = DataPage.newInstance(_classInfoState.value.data, isReload)
            val rv = GetClassTeacherListUseCase.GetClassTeacherListRV("vucuonghihi").apply {
                type = classType
                page = classInfoDataPage.page * classInfoDataPage.limitPage
                size = classInfoDataPage.limitPage
            }

            getClassTeacherListUseCase.invoke(rv)
                .onStart {
                    if (!isLoadMore) {
                        _classInfoState.loading()
                    }
                }
                .onException {
                    _classInfoState.failure(it)
                }
                .collect {
                    delay(1000L)
                    if (isReload) {
                        classInfoDataPage.clearDataPage()
                    }
                    classInfoDataPage.addAllDataList(it)
                    _classInfoState.success(classInfoDataPage)
                }
        }
    }

    fun resetState() {
        _classInfoState.reset()
    }
}
