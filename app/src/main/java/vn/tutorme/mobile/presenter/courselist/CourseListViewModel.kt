package vn.tutorme.mobile.presenter.courselist

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
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.usecase.course.GetCourseInfoListUseCase
import vn.tutorme.mobile.utils.TimeUtils
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val getCourseInfoListUseCase: GetCourseInfoListUseCase
) : BaseViewModel() {
    private val _courseListState = MutableStateFlow(FlowResult.newInstance<List<CourseInfo>>())
    val courseListState = _courseListState.asStateFlow()

    init {
        getCourseList(true)
    }

    fun getCourseList(isLoading: Boolean = false) {
        viewModelScope.launch {
            val rv = GetCourseInfoListUseCase.GetCourseInfoListRV(
                currentTime = TimeUtils.getTimeCurrent()
            )

            getCourseInfoListUseCase.invoke(rv)
                .onStart {
                    if (isLoading) {
                        _courseListState.loading()
                    }
                }
                .onException {
                    _courseListState.failure(it)
                }
                .collect {
                    _courseListState.success(it)
                }
        }
    }
}
