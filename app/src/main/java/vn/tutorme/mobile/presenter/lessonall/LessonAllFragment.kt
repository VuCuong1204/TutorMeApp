package vn.tutorme.mobile.presenter.lessonall

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setActionRoleState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LessonAllFragmentBinding
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.presenter.dialog.datepicker.DATE_TYPE
import vn.tutorme.mobile.presenter.dialog.datepicker.DatePickerDialog
import vn.tutorme.mobile.presenter.lessondetail.LessonDetailFragment
import vn.tutorme.mobile.utils.TimeUtils

@AndroidEntryPoint
class LessonAllFragment : TutorMeFragment<LessonAllFragmentBinding>(R.layout.lesson_all_fragment) {

    private val viewModel by viewModels<LessonAllViewModel>()

    private val lessonAllAdapter by lazy {
        LessonAllAdapter()
    }

    override fun onInitView() {
        super.onInitView()

        addHeader()
        addAdapter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.lessonInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    binding.cvLessonRoot.submitList(it.data)
                    viewModel.reset()
                }
            }, canShowLoading = true)
        }
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    private fun addAdapter() {
        binding.cvLessonRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(lessonAllAdapter)
        }
    }

    private fun addHeader() {
        binding.ivLessonAllBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.ivLessonAllFilter.setOnSafeClick {
            showDateDialog()
        }

        setActionRoleState(
            { binding.tvLessonAllContent.text = getAppString(R.string.calender_teach) },
            { binding.tvLessonAllContent.text = getAppString(R.string.calender_learn) }
        )

        addListener()
    }

    private fun showDateDialog() {
        val dialog = DatePickerDialog(datePickerListener = {
            val time = TimeUtils.getTimeByDay(it.timeInMillis)
            setActionRoleState(
                { viewModel.getLessonAll(time.first, time.second) },
                { viewModel.getLessonAllStudent(time.first, time.second) }
            )
        }, dateType = DATE_TYPE.PASS)
        dialog.show(childFragmentManager, TAG)
    }

    private fun addListener() {
        lessonAllAdapter.listener = object : ILessonAllListener {
            override fun onItemClick(item: LessonInfo) {
                replaceFragment(
                    fragment = LessonDetailFragment(),
                    bundle = bundleOf(
                        LessonDetailFragment.CLASS_ID_KEY to item.classId,
                        LessonDetailFragment.LESSON_ID_KEY to item.lessonId
                    )
                )
            }
        }
    }

    private fun removeListener() {
        lessonAllAdapter.listener = null
    }
}
