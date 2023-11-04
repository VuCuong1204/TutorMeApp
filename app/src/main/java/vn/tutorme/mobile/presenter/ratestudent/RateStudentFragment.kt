package vn.tutorme.mobile.presenter.ratestudent

import android.text.style.ForegroundColorSpan
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.RateStudentFragmentBinding
import vn.tutorme.mobile.domain.model.authen.EVALUATE_STATE
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.widget.rateview.IRateListener

@AndroidEntryPoint
class RateStudentFragment : TutorMeFragment<RateStudentFragmentBinding>(R.layout.rate_student_fragment) {

    companion object {
        const val USER_ID_KEY = "USER_ID_KEY"
        const val USER_NAME_KEY = "USER_NAME_KEY"
        const val USER_ROLE_KEY = "USER_ROLE_KEY"
        const val LESSON_ID_KEY = "LESSON_ID_KEY"
    }

    private val viewModel by viewModels<RateStudentViewModel>()

    private var s1: Float = 0f
    private var s2: Float = 0f
    private var s3: Float = 0f
    private var s4: Float = 0f
    private var s5: Float = 0f
    private var s6: Float = 0f

    override fun onInitView() {
        super.onInitView()

        binding.ivRateStudentBack.setOnSafeClick { onBackPressByFragment() }
        binding.tvRateStudentConfirm.setOnSafeClick { showConfirmDialog() }
        if (viewModel.userRole == EVALUATE_STATE.NO_EVALUATE_STATE) {
            setEditState()
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.lessonInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    addHeader()
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.userInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    setEditState()
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.insertReviewState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    showSuccess(getAppString(R.string.comment_success))
                    onBackPressByFragment()
                }
            }, canShowLoading = true)
        }
    }

    private fun setEditState() {
        binding.rvRateStudent1.apply {
            setInfoRate(
                viewModel.userInfo?.evaluateState ?: EVALUATE_STATE.NO_EVALUATE_STATE,
                viewModel.userInfo?.scoreAttitude,
                viewModel.userInfo?.commentAttitude)
            listener = object : IRateListener {
                override fun changeSeekbar(score: Float) {
                    s1 = score
                    val scoreMedium = sumMedium()
                    setMediumState(scoreMedium)
                }
            }
        }

        binding.rvRateStudent2.apply {
            setInfoRate(
                viewModel.userInfo?.evaluateState ?: EVALUATE_STATE.NO_EVALUATE_STATE,
                viewModel.userInfo?.scorePreparation,
                viewModel.userInfo?.commentPreparation)
            listener = object : IRateListener {
                override fun changeSeekbar(score: Float) {
                    s2 = score
                    val scoreMedium = sumMedium()
                    setMediumState(scoreMedium)
                }
            }
        }

        binding.rvRateStudent3.apply {
            setInfoRate(
                viewModel.userInfo?.evaluateState ?: EVALUATE_STATE.NO_EVALUATE_STATE,
                viewModel.userInfo?.scoreAskQuestion,
                viewModel.userInfo?.commentAskQuestion)
            listener = object : IRateListener {
                override fun changeSeekbar(score: Float) {
                    s3 = score
                    val scoreMedium = sumMedium()
                    setMediumState(scoreMedium)
                }
            }
        }

        binding.rvRateStudent4.apply {
            setInfoRate(
                viewModel.userInfo?.evaluateState ?: EVALUATE_STATE.NO_EVALUATE_STATE,
                viewModel.userInfo?.scoreJoinTheDiscussion,
                viewModel.userInfo?.commentJoinTheDiscussion)
            listener = object : IRateListener {
                override fun changeSeekbar(score: Float) {
                    s4 = score
                    val scoreMedium = sumMedium()
                    setMediumState(scoreMedium)
                }
            }
        }

        binding.rvRateStudent5.apply {
            setInfoRate(
                viewModel.userInfo?.evaluateState ?: EVALUATE_STATE.NO_EVALUATE_STATE,
                viewModel.userInfo?.scoreAttention,
                viewModel.userInfo?.commentAttention)
            listener = object : IRateListener {
                override fun changeSeekbar(score: Float) {
                    s5 = score
                    val scoreMedium = sumMedium()
                    setMediumState(scoreMedium)
                }
            }
        }

        binding.rvRateStudent6.apply {
            setInfoRate(
                viewModel.userInfo?.evaluateState ?: EVALUATE_STATE.NO_EVALUATE_STATE,
                viewModel.userInfo?.scoreCompleteTheXercise,
                viewModel.userInfo?.commentCompleteTheXercise)
            listener = object : IRateListener {
                override fun changeSeekbar(score: Float) {
                    s6 = score
                    val scoreMedium = sumMedium()
                    setMediumState(scoreMedium)
                }
            }
        }

        if (viewModel.userInfo?.evaluateState == EVALUATE_STATE.HAVE_EVALUATE_STATE) {
            binding.tvRateStudentComment.text = viewModel.userInfo?.commentMedium
            binding.tvRateStudentComment.show()
            binding.tvRateStudentConfirm.gone()
            binding.edtRateStudentComment.gone()
        } else {
            binding.tvRateStudentComment.gone()
            binding.edtRateStudentComment.show()
        }

        s1 = viewModel.userInfo?.scoreAttitude ?: 0f
        s2 = viewModel.userInfo?.scorePreparation ?: 0f
        s3 = viewModel.userInfo?.scoreAskQuestion ?: 0f
        s4 = viewModel.userInfo?.scoreJoinTheDiscussion ?: 0f
        s5 = viewModel.userInfo?.scoreAttention ?: 0f
        s6 = viewModel.userInfo?.scoreCompleteTheXercise ?: 0f

        val score = sumMedium()
        setMediumState(score)

        val text = SpannableBuilder()
            .appendText("${getAppString(R.string.student)} ")
            .withSpan(ForegroundColorSpan(getAppColor(R.color.white)))
            .appendText(viewModel.userName)
            .withSpan(ForegroundColorSpan(getAppColor(R.color.secondary_1)))
            .spannedText

        binding.tvRateStudentNameStudent.text = text
    }

    private fun sumMedium(): Float {
        return (s1 + s2 + s3 + s4 + s5 + s6) / 6
    }

    private fun setMediumState(score: Float) {
        binding.tvRateStudentMedium.text = "${getAppString(R.string.score_medium)}: $score"
        binding.tvRateStudentState.text = checkForce(score)

        val content = SpannableBuilder()
            .appendText("$score")
            .withSpan(ForegroundColorSpan(getAppColor(R.color.secondary_1)))
            .appendText("/10")
            .withSpan(ForegroundColorSpan(getAppColor(R.color.white)))
            .spannedText

        binding.tvRateStudentRatio.text = content

        binding.v12.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = binding.v12.width
                val layoutParams = binding.v11.layoutParams
                layoutParams.width = ((width / 10).toFloat() * score).toInt()
                binding.v11.layoutParams = layoutParams
                binding.v12.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun addHeader() {
        binding.tvRateStudentId.text = viewModel.lessonInfo?.classId
        binding.tvRateStudentClass.text = viewModel.lessonInfo?.getClassTitle()
        binding.tvRateStudentAdvanced.text = viewModel.lessonInfo?.level
        binding.tvRateStudentNumber.text = viewModel.lessonInfo?.getNumberMemberRatio()
        binding.tvRateStudentLesson.text = viewModel.lessonInfo?.getSession()
        binding.tvRateStudentClock.text = viewModel.lessonInfo?.getTimeLearnHour()
    }

    private fun showConfirmDialog() {
        val userInfo = UserInfo(
            scoreAttitude = s1,
            commentAttitude = binding.rvRateStudent1.getContent(),
            scorePreparation = s2,
            commentPreparation = binding.rvRateStudent2.getContent(),
            scoreAskQuestion = s3,
            commentAskQuestion = binding.rvRateStudent3.getContent(),
            scoreJoinTheDiscussion = s4,
            commentJoinTheDiscussion = binding.rvRateStudent4.getContent(),
            scoreAttention = s5,
            commentAttention = binding.rvRateStudent5.getContent(),
            scoreCompleteTheXercise = s6,
            commentCompleteTheXercise = binding.rvRateStudent6.getContent(),
            commentMedium = binding.edtRateStudentComment.text.toString().trim()
        )

        BottomSheetConfirmDialog().apply {
            title = getAppString(R.string.confirm_second)
            content = getAppString(R.string.rate_student)
            textLeft = getAppString(R.string.confirm)
            avatar = getAppDrawable(R.drawable.ic_bell_read_all)
            textRight = getAppString(R.string.cancel)
            bgTextLeft = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
            bgTextRight = getAppDrawable(R.drawable.ripple_bg_white_corner_16_stroke_1)
            clTextLeft = getAppColor(R.color.white)
            clTextRight = getAppColor(R.color.text1)
            eventLeftClick {
                viewModel.insertReviewStudent(userInfo, true)
            }
        }.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }

    private fun checkForce(score: Float): String {
        return when (score) {
            in 8.0..10.0 -> getAppString(R.string.good)
            in 6.5..8.0 -> getAppString(R.string.rather)
            in 5.0..6.5 -> getAppString(R.string.medium_type)
            in 3.0..5.0 -> getAppString(R.string.weak)
            in 0.0..3.0 -> getAppString(R.string.least)
            else -> getAppString(R.string.least)
        }
    }
}
