package vn.tutorme.mobile.presenter.lessondetail

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.extension.toast
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LessonDetailFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

@AndroidEntryPoint
class LessonDetailFragment : TutorMeFragment<LessonDetailFragmentBinding>(R.layout.lesson_detail_fragment) {

    private val viewModel by viewModels<LessonDetailViewModel>()
    private val studentLessonAdapter by lazy { StudentLessonAdapter() }
    private var isShowViewMore = false

    override fun onInitView() {
        super.onInitView()

        val userInfo = AppPreferences.userInfo?.copy(role = ROLE_TYPE.TEACHER_TYPE)
        AppPreferences.userInfo = userInfo

        addHeader()
        addAdapter()
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    override fun onBackPressByFragment() {
        if (isShowViewMore) {
            binding.rlLessonDetailSupport.gone()
            isShowViewMore = false
        } else {
            super.onBackPressByFragment()
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.studentInfoLessonState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    binding.cvLessonDetailRoot.submitList(it.data)
                }
            })
        }
    }

    private fun addAdapter() {
        binding.cvLessonDetailRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(studentLessonAdapter.apply {
                lessonType = viewModel.lessonInfo?.status ?: LESSON_STATUS.UPCOMING_STATUS
            })
        }

        addListener()
    }

    private fun addListener() {
        studentLessonAdapter.listener = object : IStudentLessonListener {
            override fun onRateClick(item: UserInfo) {
                toast("success")
            }
        }
    }

    private fun removeListener() {
        studentLessonAdapter.listener = null
    }

    private fun addHeader() {
        binding.ivLessonDetailBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.ivLessonDetailViewMore.setOnSafeClick {
            isShowViewMore = true
            binding.rlLessonDetailSupport.show()
        }

        binding.rlLessonDetailSupport.setOnSafeClick {
            isShowViewMore = false
            binding.rlLessonDetailSupport.gone()
        }

        binding.tvLessonDetailInfoRoom.setOnSafeClick { }
        binding.tvLessonDetailChatRoom.setOnSafeClick { }
        binding.tvLessonDetailSupport.setOnSafeClick { }

        setLessonInfo(viewModel.lessonInfo ?: LessonInfo())
        binding.srlLessonDetailReload.setColorSchemeResources(R.color.primary)
    }

    private fun setLessonInfo(value: LessonInfo) {
        binding.tvLessonDetailId.text = value.classId
        binding.tvLessonDetailAvatar.setImageDrawable(
            when (value.status) {
                LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.ic_upcoming)
                LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.ic_happenning)
                LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.ic_cancel)
                LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.ic_took_place)
                else -> getAppDrawable(R.drawable.ic_upcoming)
            }
        )
        binding.tvLessonDetailState.background = when (value.status) {
            LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.shape_bg_secondary_corner_8)
            LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.shape_bg_status_manual_corner_10)
            LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.shape_bg_classic_corner_10)
            LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.shape_bg_state_sucess_corner_8)
            else -> getAppDrawable(R.drawable.shape_bg_secondary_corner_8)
        }

        binding.tvLessonDetailState.text = when (value.status) {
            LESSON_STATUS.UPCOMING_STATUS -> getAppString(R.string.upcoming)
            LESSON_STATUS.HAPPENING_STATUS -> getAppString(R.string.happening)
            LESSON_STATUS.CANCEL_STATUS -> getAppString(R.string.cancel)
            LESSON_STATUS.TOOK_PLACE_STATUS -> getAppString(R.string.took_place)
            else -> getAppString(R.string.upcoming)
        }

        binding.tvLessonDetailClass.text = value.getClassTitle()
        binding.tvLessonDetailAdvanced.text = value.level
        binding.tvLessonDetailLesson.text = value.getSession()

        binding.tvLessonDetailNumber.text = if (value.status == LESSON_STATUS.TOOK_PLACE_STATUS) {
            value.getNumberMemberRatio()
        } else {
            value.getNumberMember()
        }
    }
}
