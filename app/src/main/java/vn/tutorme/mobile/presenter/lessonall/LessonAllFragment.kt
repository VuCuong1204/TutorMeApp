package vn.tutorme.mobile.presenter.lessonall

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

        setActionRoleState(
            { binding.tvLessonAllContent.text = getAppString(R.string.calender_teach) },
            { binding.tvLessonAllContent.text = getAppString(R.string.calender_learn) }
        )
    }
}
