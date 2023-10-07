package vn.tutorme.mobile.presenter.lessonevaluate

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

    private fun addAdapter() {
        binding.cvLessonEvaluateRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(lessonEvaluateAdapter)
        }
    }

    private fun addHeader() {
        binding.ivLessonEvaluateAllBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }
}
