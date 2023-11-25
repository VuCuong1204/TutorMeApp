package vn.tutorme.mobile.presenter.profile.schedule

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.profile.DAY_TYPE
import vn.tutorme.mobile.domain.model.profile.schedule.ScheduleInfo
import vn.tutorme.mobile.domain.usecase.lesson.GetLessonInfoStudentUseCase
import vn.tutorme.mobile.domain.usecase.lesson.GetLessonInfoTeacherUseCase
import vn.tutorme.mobile.utils.TimeUtils
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getLessonInfoStudentUseCase: GetLessonInfoStudentUseCase,
    private val getLessonInfoTeacherUseCase: GetLessonInfoTeacherUseCase,
) : BaseViewModel() {
    private val _lessonState = MutableStateFlow(FlowResult.newInstance<List<ScheduleInfo>>())
    val lessonState = _lessonState.asStateFlow()

    init {
        if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
            getScheduleInfoTeacher()
        } else {
            getScheduleInfoStudent()
        }
    }

    private fun getScheduleInfoStudent() {
        viewModelScope.launch {
            val rv = GetLessonInfoStudentUseCase.GetLessonInfoStudentRV().apply {
                startTime = TimeUtils.getStartOfWeek()
                endTime = TimeUtils.getEndOfWeek()
            }

            getLessonInfoStudentUseCase.invoke(rv)
                .onStart {
                    _lessonState.loading()
                }
                .onException {
                    _lessonState.failure(it)
                }
                .collect {
                    val data = mapperLessonInfoToSchedule(it)
                    _lessonState.success(data)
                }
        }
    }

    private fun getScheduleInfoTeacher() {
        viewModelScope.launch {
            val rv = GetLessonInfoTeacherUseCase.GetLessonInfoTeacherRV().apply {
                startTime = TimeUtils.getStartOfWeek()
                endTime = TimeUtils.getEndOfWeek()
            }

            getLessonInfoTeacherUseCase.invoke(rv)
                .onStart {
                    _lessonState.loading()
                }
                .onException {
                    _lessonState.failure(it)
                }
                .collect {
                    val data = mapperLessonInfoToSchedule(it)
                    _lessonState.success(data)
                }
        }
    }

    private fun mapperLessonInfoToSchedule(dataList: List<LessonInfo>): List<ScheduleInfo> {
        val list = mutableListOf<ScheduleInfo>()
        dataList.forEach {
            list.add(ScheduleInfo(
                timeBegin = TimeUtils.getTimeMinute(it.timeBeginLesson ?: LONG_DEFAULT),
                timeEnd = TimeUtils.getTimeMinute(it.timeEndLesson ?: LONG_DEFAULT),
                rank = DAY_TYPE.valueOfName(TimeUtils.getRankInWeek(it.timeBeginLesson ?: LONG_DEFAULT)),
                classId = it.classId,
                className = it.getClassTitle(),
                lessonCount = it.lessonSession,
                content = it.level
            ))
        }

        return list
    }

    fun getColorList() = listOf(
        R.color.red,
        R.color.gray,
        R.color.blue,
        R.color.green,
        R.color.yellow_20,
        R.color.status_warring,
        R.color.blue_10
    )
}
