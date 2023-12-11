package vn.tutorme.mobile.presenter.ratestudent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.tutorme.mobile.base.common.BaseViewModel
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.failure
import vn.tutorme.mobile.base.extension.loading
import vn.tutorme.mobile.base.extension.onException
import vn.tutorme.mobile.base.extension.success
import vn.tutorme.mobile.domain.model.authen.EVALUATE_STATE
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.usecase.lesson.GetLessonDetailUseCase
import vn.tutorme.mobile.domain.usecase.lesson.GetReviewDetailUseCase
import vn.tutorme.mobile.domain.usecase.lesson.InsertReviewDetailUseCase
import vn.tutorme.mobile.presenter.ratestudent.RateStudentFragment.Companion.LESSON_ID_KEY
import vn.tutorme.mobile.presenter.ratestudent.RateStudentFragment.Companion.USER_ID_KEY
import vn.tutorme.mobile.presenter.ratestudent.RateStudentFragment.Companion.USER_NAME_KEY
import vn.tutorme.mobile.presenter.ratestudent.RateStudentFragment.Companion.USER_EVALUATE_KEY
import javax.inject.Inject

@HiltViewModel
class RateStudentViewModel @Inject constructor(
    private val insertReviewDetailUseCase: InsertReviewDetailUseCase,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val getLessonDetailUseCase: GetLessonDetailUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _userInfoState = MutableStateFlow(FlowResult.newInstance<UserInfo>())
    val userInfoState = _userInfoState.asStateFlow()

    private val _lessonInfoState = MutableStateFlow(FlowResult.newInstance<LessonInfo>())
    val lessonInfoState = _lessonInfoState.asStateFlow()

    private val _insertReviewState = MutableStateFlow(FlowResult.newInstance<Boolean>())
    val insertReviewState = _insertReviewState.asStateFlow()

    var userId = savedStateHandle.get<String>(USER_ID_KEY) ?: STRING_DEFAULT
    var userName = savedStateHandle.get<String>(USER_NAME_KEY) ?: STRING_DEFAULT
    var userEvaluate = savedStateHandle.get<EVALUATE_STATE>(USER_EVALUATE_KEY)
        ?: EVALUATE_STATE.HAVE_EVALUATE_STATE
    var lessonId = savedStateHandle.get<Int>(LESSON_ID_KEY) ?: INT_DEFAULT

    var userInfo: UserInfo? = null
    var lessonInfo: LessonInfo? = null

    init {
        getLessonDetail(true)
    }

    fun insertReviewStudent(userInfo: UserInfo, isLoading: Boolean = false) {
        viewModelScope.launch {
            val rv = InsertReviewDetailUseCase.InsertReviewDetailVH(
                scoreAttitude = userInfo.scoreAttitude ?: 0f,
                commentAttitude = userInfo.commentAttitude ?: STRING_DEFAULT,
                scorePreparation = userInfo.scoreAttitude ?: 0f,
                commentPreparation = userInfo.commentPreparation ?: STRING_DEFAULT,
                scoreAskQuestion = userInfo.scoreAskQuestion ?: 0f,
                commentAskQuestion = userInfo.commentAskQuestion ?: STRING_DEFAULT,
                scoreJoinTheDiscussion = userInfo.scoreJoinTheDiscussion ?: 0f,
                commentJoinTheDiscussion = userInfo.commentJoinTheDiscussion ?: STRING_DEFAULT,
                scoreAttention = userInfo.scoreAttention ?: 0f,
                commentAttention = userInfo.commentAttention ?: STRING_DEFAULT,
                scoreCompleteTheXercise = userInfo.scoreCompleteTheXercise ?: 0f,
                commentCompleteTheXercise = userInfo.commentCompleteTheXercise ?: STRING_DEFAULT,
                commentMedium = userInfo.commentMedium ?: STRING_DEFAULT,
                userId = userId,
                lessonId = lessonId
            )

            insertReviewDetailUseCase.invoke(rv)
                .onStart {
                    if (isLoading) {
                        _insertReviewState.loading()
                    }
                }
                .onException {
                    _insertReviewState.failure(it)
                }
                .collect {
                    _insertReviewState.success(it)
                }
        }
    }

    fun getReviewDetail(isLoading: Boolean = false) {
        viewModelScope.launch {
            val rv = GetReviewDetailUseCase.GetReviewDetailRV(userId, lessonId)
            getReviewDetailUseCase.invoke(rv)
                .onStart {
                    if (isLoading) {
                        _userInfoState.loading()
                    }
                }
                .onException {
                    _userInfoState.failure(it)
                }
                .collect {
                    userInfo = it.copy(evaluateState = userEvaluate, fullName = userName)
                    _userInfoState.success(it)
                }
        }
    }

    fun getLessonDetail(isReload: Boolean = false) {
        viewModelScope.launch {
            val rv = GetLessonDetailUseCase.GetLessonDetailRV(lessonId)
            getLessonDetailUseCase.invoke(rv)
                .onStart {
                    if (isReload) {
                        _lessonInfoState.loading()
                    }
                }
                .onException {
                    _lessonInfoState.failure(it)
                }
                .collect {
                    if (userEvaluate == EVALUATE_STATE.HAVE_EVALUATE_STATE) {
                        getReviewDetail(false)
                    }
                    lessonInfo = it
                    _lessonInfoState.success(it)
                }
        }
    }
}
