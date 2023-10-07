package vn.tutorme.mobile.presenter.lessonall

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
import vn.tutorme.mobile.domain.usecase.GetLessonAllUserCase
import javax.inject.Inject

@HiltViewModel
class LessonAllViewModel @Inject constructor(
    private val getLessonAllUserCase: GetLessonAllUserCase
) : BaseViewModel() {
    private val _lessonInfoState = MutableStateFlow(FlowResult.newInstance<List<Any>>())
    val lessonInfoState = _lessonInfoState.asStateFlow()

    init {
        getLessonAll()
    }

    private fun getLessonAll() {
        viewModelScope.launch {
            delay(100)
            val rv = GetLessonAllUserCase.GetLessonAllRV("vucuonghihi", 1696788000, 16973928000)
            getLessonAllUserCase.invoke(rv)
                .onStart {
                    _lessonInfoState.loading()
                }
                .onException {
                    _lessonInfoState.failure(it)
                }
                .collect {
                    _lessonInfoState.success(it)
                }
        }
    }

    fun reset() {
        _lessonInfoState.reset()
    }
}
