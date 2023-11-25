package vn.tutorme.mobile.presenter.lessonall

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
import vn.tutorme.mobile.base.extension.setActionRoleState
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.usecase.GetLessonAllStudentUseCase
import vn.tutorme.mobile.domain.usecase.GetLessonAllUserCase
import vn.tutorme.mobile.utils.TimeUtils
import javax.inject.Inject

@HiltViewModel
class LessonAllViewModel @Inject constructor(
    private val getLessonAllUserCase: GetLessonAllUserCase,
    private val getLessonAllStudentUseCase: GetLessonAllStudentUseCase
) : BaseViewModel() {

    companion object {
        const val TIME_DELAY = 100L
    }

    private val _lessonInfoState = MutableStateFlow(FlowResult.newInstance<List<Any>>())
    val lessonInfoState = _lessonInfoState.asStateFlow()

    init {
        setActionRoleState(
            { getLessonAll() },
            { getLessonAllStudent() }
        )
    }

    fun getLessonAll(
        beginTime: Long = TimeUtils.getStartOfDay(),
        endTime: Long = TimeUtils.getNextDay()
    ) {
        viewModelScope.launch {
            delay(TIME_DELAY)
            val rv = GetLessonAllUserCase.GetLessonAllRV(
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT,
                beginTime,
                endTime
            )

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

     fun getLessonAllStudent(
        beginTime: Long = TimeUtils.getStartOfDay(),
        endTime: Long = TimeUtils.getNextDay()
    ) {
        viewModelScope.launch {
            delay(TIME_DELAY)
            val rv = GetLessonAllStudentUseCase.GetLessonAllStudentVH(
                AppPreferences.userInfo?.userId ?: STRING_DEFAULT,
                beginTime,
                endTime
            )

            getLessonAllStudentUseCase.invoke(rv)
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
