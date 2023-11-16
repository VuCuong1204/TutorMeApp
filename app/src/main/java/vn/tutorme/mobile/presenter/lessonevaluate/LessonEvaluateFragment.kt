package vn.tutorme.mobile.presenter.lessonevaluate

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.LessonEvaluateFragmentBinding
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.presenter.lessondetail.LessonDetailFragment

@AndroidEntryPoint
class LessonEvaluateFragment : TutorMeFragment<LessonEvaluateFragmentBinding>(R.layout.lesson_evaluate_fragment) {

    private val viewModel by viewModels<LessonEvaluateViewModel>()
    private val lessonEvaluateAdapter by lazy { LessonEvaluateAdapter() }

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
                    binding.cvLessonEvaluateRoot.submitList(it.data)
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
        binding.cvLessonEvaluateRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(lessonEvaluateAdapter)
        }

        addListener()
    }

    private fun addHeader() {
        binding.ivLessonEvaluateAllBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }

    private fun addListener() {
        lessonEvaluateAdapter.listener = object : ILessonEvaluateListener {
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
        lessonEvaluateAdapter.listener = null
    }
}
