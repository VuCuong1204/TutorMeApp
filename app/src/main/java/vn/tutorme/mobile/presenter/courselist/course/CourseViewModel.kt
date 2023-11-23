package vn.tutorme.mobile.presenter.courselist.course

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.usecase.course.CheckCourseRegisteredUseCase
import vn.tutorme.mobile.domain.usecase.course.GetClassListFromCourseUseCase
import vn.tutorme.mobile.domain.usecase.course.GetCourseInfoUseCase
import vn.tutorme.mobile.domain.usecase.course.RegisterCourseUseCase
import vn.tutorme.mobile.presenter.courselist.course.CourseFragment.Companion.COURSE_ID_KEY
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val getCourseInfoUseCase: GetCourseInfoUseCase,
    private val getClassListFromCourseUseCase: GetClassListFromCourseUseCase,
    private val checkCourseRegisteredUseCase: CheckCourseRegisteredUseCase,
    private val registerCourseUseCase: RegisterCourseUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _courseInfoState = MutableStateFlow(FlowResult.newInstance<CourseInfo>())
    val courseInfoState = _courseInfoState.asStateFlow()

    private val _classInfoState = MutableStateFlow(FlowResult.newInstance<List<ClassInfo>>())
    val classInfoState = _classInfoState.asStateFlow()

    private val _checkRegisteredState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val checkRegisteredState = _checkRegisteredState.asStateFlow()

    private val _registerCourseState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val registerCourseState = _registerCourseState.asStateFlow()

    var courseId = savedStateHandle.get<String>(COURSE_ID_KEY) ?: STRING_DEFAULT

    init {
        getCourseInfo(courseId)
        checkCourseRegister(courseId, AppPreferences.userInfo?.userId ?: STRING_DEFAULT)
    }

    private fun getCourseInfo(courseId: String) {
        viewModelScope.launch {
            val rv = GetCourseInfoUseCase.GetCourseInfoRV(courseId)
            getCourseInfoUseCase.invoke(rv)
                .onStart {
                    _courseInfoState.loading()
                }
                .onException {
                    _courseInfoState.failure(it)
                }
                .collect {
                    _courseInfoState.success(it)
                }
        }
    }

    fun getClassListFromCourse(courseId: String) {
        viewModelScope.launch {
            val rv = GetClassListFromCourseUseCase.GetClassListFromCourseRV(courseId)
            getClassListFromCourseUseCase.invoke(rv)
                .onStart {
                    _classInfoState.loading()
                }
                .onException {
                    _classInfoState.failure(it)
                }
                .collect {
                    _classInfoState.success(it)
                }
        }
    }

    private fun checkCourseRegister(courseId: String, userId: String) {
        viewModelScope.launch {
            val rv = CheckCourseRegisteredUseCase.CheckCourseRegisteredRV(courseId, userId)
            checkCourseRegisteredUseCase.invoke(rv)
                .onException {
                    _checkRegisteredState.failure(it)
                }
                .collect {
                    _checkRegisteredState.success(it)
                }
        }
    }

    fun registerCourse(classId: String, studentId: String, lessonFirst: Int, lessonSecond: Int) {
        viewModelScope.launch {
            val rv = RegisterCourseUseCase.RegisterCourseRV(classId, studentId, lessonFirst, lessonSecond)
            registerCourseUseCase.invoke(rv)
                .onException {
                    _registerCourseState.failure(it)
                }
                .collect {
                    _registerCourseState.success(it)
                }
        }
    }
}
