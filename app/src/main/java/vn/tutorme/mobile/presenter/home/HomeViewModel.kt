package vn.tutorme.mobile.presenter.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.data
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.reset
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.base.model.DataPage
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_TYPE
import vn.tutorme.mobile.domain.usecase.GetHomeStudentUseCase
import vn.tutorme.mobile.domain.usecase.GetHomeTeacherUseCase
import vn.tutorme.mobile.domain.usecase.UpdateStateClassRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeTeacherUseCase: GetHomeTeacherUseCase,
    private val getHomeStudentUseCase: GetHomeStudentUseCase,
    private val updateStateClassRegisterUseCase: UpdateStateClassRegisterUseCase
) : BaseViewModel() {
    private val _homeState = MutableStateFlow(FlowResult.newInstance<DataPage<Any>>())
    val homeState = _homeState.asStateFlow()
    private var homeDataList = DataPage.newInstance(_homeState.data(), true)

    init {
        if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
            getHomeStudent()
        } else {
            getHomeTeacher()
        }
    }

    fun getHomeTeacher(reload: Boolean = false) {
        viewModelScope.launch {
            homeDataList = DataPage.newInstance(_homeState.value.data, reload)
            val rv = GetHomeTeacherUseCase.GetHomeTeacherRV("vucuonghihi").apply {
                sizeSchedule = 10
                sizeEvaluator = 10
                beginTimeSchedule = 1696788000
                endTimeSchedule = 1696874400
                beginTimeEvaluated = 1696788000
                endTimeEvaluated = 1697392800
                currentTime = 1696269600
                stateRate = LESSON_TYPE.NOT_YET_RATE_TYPE
                stateClass = CLASS_STATUS.EMPTY_CLASS_STATUS
            }

            getHomeTeacherUseCase.invoke(rv)
                .onStart {
                    if (!reload) {
                        _homeState.loading()
                    }
                }
                .onException {
                    _homeState.failure(it)
                }
                .collect {
                    homeDataList.replaceDataList(it)
                    _homeState.success(homeDataList)
                }
        }
    }

    fun getHomeStudent(reload: Boolean = false) {
        viewModelScope.launch {
            homeDataList = DataPage.newInstance(_homeState.value.data, reload)
            val rv = GetHomeStudentUseCase.GetHomeTeacherRV("vq3", 1696402317, 1696788000, 1697392800)
            getHomeStudentUseCase.invoke(rv)
                .onStart {
                    if (!reload) {
                        _homeState.loading()
                    }
                }
                .onException {
                    _homeState.failure(it)
                }
                .collect {
                    homeDataList.replaceDataList(it)
                    _homeState.success(homeDataList)
                }
        }
    }

    fun updateClassRegister(classId: String) {
        viewModelScope.launch {
            val list = homeDataList.dataList.toMutableList()
            val rv = UpdateStateClassRegisterUseCase.UpdateStateClassRegisterRV(
                classId,
                0,
                "Vucuonghihi"
            )

            updateStateClassRegisterUseCase.invoke(rv)
                .onStart {
                    _homeState.loading()
                }
                .onException {
                    _homeState.failure(it)
                }
                .collect {
                    val dataNew = it.toMutableList()
                    if (it.isEmpty()) dataNew.add(ClassInfo())
                    list.removeLast()
                    list.add(list.size, dataNew)
                    homeDataList.replaceDataList(list)
                    _homeState.success(homeDataList)
                }
        }
    }

    fun resetState() {
        _homeState.reset()
    }
}
