package vn.tutorme.mobile.presenter.lessondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.detectinfo.DetectInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.notification.DeviceInfo
import vn.tutorme.mobile.domain.usecase.lesson.AttendanceStudentUseCase
import vn.tutorme.mobile.domain.usecase.lesson.FeedBackLessonUseCase
import vn.tutorme.mobile.domain.usecase.lesson.GetFeedbackListUseCase
import vn.tutorme.mobile.domain.usecase.lesson.GetLessonDetailUseCase
import vn.tutorme.mobile.domain.usecase.lesson.GetStudentInLessonUseCase
import vn.tutorme.mobile.domain.usecase.lesson.UpdateStateLessonUseCase
import vn.tutorme.mobile.domain.usecase.notification.SendNotificationBeginLessonUseCase
import vn.tutorme.mobile.domain.usecase.tensorflow.SendFaceDetectImageUseCase
import vn.tutorme.mobile.presenter.lessondetail.LessonDetailFragment.Companion.CLASS_ID_KEY
import vn.tutorme.mobile.presenter.lessondetail.LessonDetailFragment.Companion.LESSON_ID_KEY
import vn.tutorme.mobile.presenter.lessondetail.model.LESSON_DETAIL_TYPE
import vn.tutorme.mobile.presenter.lessondetail.model.LessonTypeDisplay
import vn.tutorme.mobile.presenter.lessondetail.model.ZoomRoomInfo
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val getLessonDetailUseCase: GetLessonDetailUseCase,
    private val updateStateLessonUseCase: UpdateStateLessonUseCase,
    private val getFeedbackListUseCase: GetFeedbackListUseCase,
    private val feedBackLessonUseCase: FeedBackLessonUseCase,
    private val attendanceStudentUseCase: AttendanceStudentUseCase,
    private val getStudentInLessonUseCase: GetStudentInLessonUseCase,
    private val sendFaceDetectImageUseCase: SendFaceDetectImageUseCase,
    private val sendNotificationBeginLessonUseCase: SendNotificationBeginLessonUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _lessonDetailState = MutableStateFlow(FlowResult.newInstance<LessonInfo>())
    val lessonDetailState = _lessonDetailState.asStateFlow()

    private val _studentInfoLessonState = MutableStateFlow(FlowResult.newInstance<List<Any>>())
    val studentInfoLessonState = _studentInfoLessonState.asStateFlow()

    private val _feedbackListState = MutableStateFlow(FlowResult.newInstance<Any>())
    val feedbackListState = _feedbackListState.asStateFlow()

    private val _attendanceStudentState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val attendanceStudentState = _attendanceStudentState.asStateFlow()

    private val _detectInfoState = MutableStateFlow(FlowResult.newInstance<DetectInfo>())
    val detectInfoState = _detectInfoState.asStateFlow()

    private val _notificationState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val notificationState = _notificationState.asStateFlow()

    var lessonId = savedStateHandle.get<Int>(LESSON_ID_KEY) ?: INT_DEFAULT
    var classId = savedStateHandle.get<String>(CLASS_ID_KEY) ?: STRING_DEFAULT
    var lessonInfo: LessonInfo? = null
    private var studentInfoLesson = mutableListOf<UserInfo>()
    var zoomRoomInfo: ZoomRoomInfo? = null

    init {
        getLessonDetail(true)
    }

    fun getLessonDetail(isReload: Boolean = false) {
        viewModelScope.launch {
            val rv = GetLessonDetailUseCase.GetLessonDetailRV(lessonId)
            getLessonDetailUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _lessonDetailState.loading()
                    }
                }
                .onException {
                    _lessonDetailState.failure(it)
                }
                .collect {
                    lessonInfo = it
                    getStudentInfoLesson(false)
                    _lessonDetailState.success(it)
                }
        }
    }

    fun getStudentInfoLesson(isReload: Boolean = false) {
        viewModelScope.launch {
            val rv = GetStudentInLessonUseCase.GetStudentInLessonRV(classId)
            getStudentInLessonUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _studentInfoLessonState.loading()
                    }
                }
                .onException {
                    _studentInfoLessonState.failure(it)
                }
                .collect {
                    studentInfoLesson = it.toMutableList()
                    val dataList = mutableListOf<Any>()
                    val userInfo = studentInfoLesson.find { it.userId == AppPreferences.userInfo?.userId }
                        ?: AppPreferences.userInfo
                    if (lessonInfo?.status == LESSON_STATUS.TOOK_PLACE_STATUS) {
                        dataList.add(LessonTypeDisplay(lessonInfo, userInfo, LESSON_STATUS.TOOK_PLACE_STATUS))
                    } else {
                        dataList.add(LessonTypeDisplay(lessonInfo, userInfo, LESSON_STATUS.UPCOMING_STATUS))
                    }

                    dataList.add(LESSON_DETAIL_TYPE.TITLE_TYPE)

                    dataList.addAll(it)
                    _studentInfoLessonState.success(dataList)
                }
        }
    }

    fun updateStateLesson(isReload: Boolean = false, state: LESSON_STATUS) {
        viewModelScope.launch {
            val rv = UpdateStateLessonUseCase.UpdateStateLessonRV(lessonId, state)
            updateStateLessonUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _lessonDetailState.loading()
                    }
                }
                .onException {
                    _lessonDetailState.failure(it)
                }
                .collect {
                    lessonInfo = it
                    getStudentInfoLesson(false)
                    _lessonDetailState.success(it)
                }
        }
    }

    fun getFeedbackList(isReload: Boolean = false) {
        viewModelScope.launch {
            val rv = GetFeedbackListUseCase.GetFeedbackListRV(lessonId)
            getFeedbackListUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _feedbackListState.loading()
                    }
                }
                .onException {
                    _feedbackListState.failure(it)
                }
                .collect {
                    _feedbackListState.success(it)
                }
        }
    }

    fun sendFaceDetectImage(uri: String) {
        viewModelScope.launch {
            val rv = SendFaceDetectImageUseCase.SendFaceDetectImageRV(uri)
            sendFaceDetectImageUseCase.invoke(rv)
                .onStart {
                    _detectInfoState.loading()
                }
                .onException {
                    _detectInfoState.failure(it)
                }
                .collect {
                    _detectInfoState.success(it)
                }
        }
    }

    fun attendanceStudent(isReload: Boolean = false, studentId: String) {
        viewModelScope.launch {
            val rv = AttendanceStudentUseCase.AttendanceStudentRV(lessonId, studentId)
            attendanceStudentUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _attendanceStudentState.loading()
                    }
                }
                .onException {
                    _attendanceStudentState.failure(it)
                }
                .collect {
                    _attendanceStudentState.success(it)
                }
        }
    }

    fun feedBackLessonUseCase(isReload: Boolean = false, content: String) {
        viewModelScope.launch {
            val rv = FeedBackLessonUseCase.FeedBackLessonRV(lessonId, content)
            feedBackLessonUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _feedbackListState.loading()
                    }
                }
                .onException {
                    _feedbackListState.failure(it)
                }
                .collect {
                    _feedbackListState.success(it)
                }
        }
    }

    fun sendNotificationToUser(
        dataList: List<DeviceInfo>,
        lessonId: String,
        classId: String,
        title: String,
        body: String
    ) {
        viewModelScope.launch {
            val deviceIdList = mutableListOf<String>()
            studentInfoLesson.forEach { userInfo ->
                for (deviceInfo in dataList.iterator()) {
                    if (userInfo.userId == deviceInfo.userId) {
                        deviceInfo.deviceId?.let { deviceIdList.add(it) }
                        break
                    }
                }
            }

            val gson = Gson()
            val jsonDeviceIdString = gson.toJson(deviceIdList)

            val rv = SendNotificationBeginLessonUseCase.SendNotificationBeginLessonRV(
                jsonDeviceIdString, lessonId, classId, title, body
            )

            sendNotificationBeginLessonUseCase.invoke(rv)
                .onException {
                    _notificationState.failure(it)
                }
                .collect {
                    _notificationState.success(it)
                }
        }
    }
}
