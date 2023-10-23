package vn.tutorme.mobile.presenter.classall

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassAllFragmentBinding

@AndroidEntryPoint
class ClassAllFragment : TutorMeFragment<ClassAllFragmentBinding>(R.layout.class_all_fragment) {

    private val viewModel by viewModels<ClassAllViewModel>()
    private val classAllAdapter by lazy { ClassAllAdapter() }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.classInfoState) {
            handleUiState(it, object : IViewListener {

                override fun onFailure() {
                    super.onFailure()
                    binding.srlLessonAllReload.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvLessonAllRoot.submitList(it.data?.dataList)
                    binding.srlLessonAllReload.isRefreshing = false
                    viewModel.reset()
                }
            }, canShowLoading = true)
        }
    }

    private fun addAdapter() {
        binding.cvLessonAllRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(classAllAdapter)
        }

        binding.srlLessonAllReload.setOnRefreshListener {
            viewModel.getClassInfo(true)
        }
    }

    private fun addHeader() {
        binding.srlLessonAllReload.setColorSchemeResources(R.color.primary)
        binding.ivClassAllBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }
}
