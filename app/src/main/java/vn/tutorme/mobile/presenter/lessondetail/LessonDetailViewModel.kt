package vn.tutorme.mobile.presenter.lessondetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.authen.mockDataUserInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.lesson.mockDataLessonInfoEvaluate
import vn.tutorme.mobile.presenter.lessondetail.model.LESSON_DETAIL_TYPE
import vn.tutorme.mobile.presenter.lessondetail.model.LessonTypeDisplay
import vn.tutorme.mobile.presenter.lessondetail.model.ZoomRoomInfo
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor() : BaseViewModel() {

    private val _studentInfoLessonState = MutableStateFlow(FlowResult.newInstance<List<Any>>())
    val studentInfoLessonState = _studentInfoLessonState.asStateFlow()

    var lessonInfo: LessonInfo? = mockDataLessonInfoEvaluate()[0]
    val studentInfoLesson = mockDataUserInfo()
    var zoomRoomInfo: ZoomRoomInfo? = null

    init {
        getStudentInfoLesson(true)
    }

    private fun getStudentInfoLesson(isReload: Boolean = false) {
        viewModelScope.launch {
            val dataList = mutableListOf<Any>()
            val userInfo = studentInfoLesson.find { it.userId == AppPreferences.userInfo?.userId }
                ?: AppPreferences.userInfo
            if (lessonInfo?.status == LESSON_STATUS.TOOK_PLACE_STATUS) {
                dataList.add(LessonTypeDisplay(lessonInfo, userInfo, LESSON_STATUS.TOOK_PLACE_STATUS))
            } else {
                dataList.add(LessonTypeDisplay(lessonInfo, userInfo, LESSON_STATUS.UPCOMING_STATUS))
            }

            dataList.add(LESSON_DETAIL_TYPE.TITLE_TYPE)

            dataList.addAll(studentInfoLesson)
            _studentInfoLessonState.success(dataList)
        }
    }
}
