package vn.tutorme.mobile.presenter.classinfo

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassInfoFragmentBinding
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.presenter.chat.ChatFragment
import vn.tutorme.mobile.presenter.lessondetail.LessonDetailFragment

@AndroidEntryPoint
class ClassInfoFragment : TutorMeFragment<ClassInfoFragmentBinding>(R.layout.class_info_fragment) {

    companion object {
        const val CLASS_ID_KEY = "CLASS_ID_KEY"
    }

    private val viewModel by viewModels<ClassInfoViewModel>()
    private val classInfoAdapter by lazy { ClassInfoAdapter() }
    var classId: String = STRING_DEFAULT

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.lessonInfoState) {
            handleUiState(it, object : IViewListener {

                override fun onFailure() {
                    super.onFailure()
                    binding.srlClassInfoReload.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvClassInfoRoot.submitList(it.data?.dataList)
                    if (!it.data?.dataList.isNullOrEmpty()) {
                        setClassInfo(it.data?.dataList)
                    }

                    binding.srlClassInfoReload.isRefreshing = false
                }
            }, canShowLoading = true)
        }
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    private fun addHeader() {
        binding.srlClassInfoReload.setColorSchemeResources(R.color.primary)
        binding.ivClassInfoBack.setOnSafeClick {
            onBackPressByFragment()
        }

        addListener()
    }

    private fun addAdapter() {
        binding.cvClassInfoRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(classInfoAdapter)
        }

        binding.srlClassInfoReload.setOnRefreshListener {
            viewModel.getLessonList(true)
        }
    }

    private fun addListener() {
        classInfoAdapter.listener = object : IClassInfoListener {
            override fun onItemClick(item: LessonInfo) {
                replaceFragment(
                    fragment = LessonDetailFragment(),
                    bundle = bundleOf(
                        LessonDetailFragment.CLASS_ID_KEY to item.classId,
                        LessonDetailFragment.LESSON_ID_KEY to item.lessonId
                    ),
                    screenAnim = SlideAnimation()
                )
            }
        }
    }

    private fun removeListener() {
        classInfoAdapter.listener = null
    }

    private fun setClassInfo(dataList: MutableList<Any>?) {
        val lessonInfo = dataList?.get(1) as? LessonInfo
        var countLesson = 0
        dataList?.forEach { if (it is LessonInfo) countLesson++ }
        with(binding) {
            tvClassInfoTime.text = lessonInfo?.getDayBegin()
            tvClassInfoId.text = lessonInfo?.classId
            tvClassInfoState.text = when (lessonInfo?.status) {
                LESSON_STATUS.UPCOMING_STATUS -> getAppString(R.string.upcoming)
                LESSON_STATUS.HAPPENING_STATUS -> getAppString(R.string.happening)
                else -> getAppString(R.string.happening)
            }
            tvClassInfoClass.text = lessonInfo?.getClassTitle()
            tvClassInfoNumber.text = lessonInfo?.getNumberMember()
            tvClassInfoAdvanced.text = lessonInfo?.level
            tvClassInfoLesson.text = String.format(getAppString(R.string.count_lesson), countLesson)
            tvClassInfoTeacher.text = lessonInfo?.getNameStudent()
            tvClassInfoTeacherNumber.text = String.format(getAppString(R.string.number_phone_teacher), lessonInfo?.phoneNumberTeacher)

            binding.tvClassInfoTeacher.setOnSafeClick {
                if (!lessonInfo?.getNameStudent().isNullOrEmpty()) {
                    replaceFragment(fragment = ChatFragment(), bundleOf(
                        ChatFragment.USER_ID_KEY to lessonInfo?.idTeacher,
                        ChatFragment.USER_NAME_KEY to lessonInfo?.nameTeacher,
                        ChatFragment.USER_AVATAR_KEY to lessonInfo?.avatarTeacher
                    ))
                }
            }
        }
    }
}
