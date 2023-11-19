package vn.tutorme.mobile.presenter.classinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.reset
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.usecase.GetLessonListInClassUseCase
import javax.inject.Inject

@HiltViewModel
class ClassInfoViewModel @Inject constructor(
    private val getLessonListInClassUseCase: GetLessonListInClassUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    companion object {
        const val TIME_DELAY = 100L
    }

    private val _lessonInfoState = MutableStateFlow(FlowResult.newInstance<DataPage<Any>>())
    val lessonInfoState = _lessonInfoState.asStateFlow()
    private var lessonDataPage = DataPage.newInstance(_lessonInfoState.value.data, true)

    val classId = savedStateHandle.get<String>(ClassInfoFragment.CLASS_ID_KEY) ?: STRING_DEFAULT

    init {
        getLessonList()
    }

    fun getLessonList(isReload: Boolean = false) {
        viewModelScope.launch {
            delay(TIME_DELAY)
            lessonDataPage = DataPage.newInstance(_lessonInfoState.value.data, isReload)
            val rv = GetLessonListInClassUseCase.GetLessonListInClassVH(classId)
            getLessonListInClassUseCase.invoke(rv)
                .onStart {
                    if (!isReload) {
                        _lessonInfoState.loading()
                    }
                }
                .onException {
                    _lessonInfoState.failure(it)
                }
                .collect {
                    lessonDataPage.replaceDataList(it)
                    _lessonInfoState.success(lessonDataPage)
                }
        }
    }

    fun resetState() {
        _lessonInfoState.reset()
    }
}
