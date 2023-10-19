package vn.tutorme.mobile.presenter.classmanager

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassManagerFragmentBinding
import vn.tutorme.mobile.domain.model.category.getDataCategoryClassType

@AndroidEntryPoint
class ClassManagerFragment : TutorMeFragment<ClassManagerFragmentBinding>(R.layout.class_manager_fragment) {

    private val viewModel by viewModels<ClassManagerViewModel>()
    private val classManagerAdapter by lazy {
        ClassManagerAdapter()
    }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.classInfoState) {
            handleUiState(it, object : IViewListener {

                override fun onLoading() {
                    binding.cvClassManagerContent.clearData()
                    binding.cvClassManagerContent.showLoading()
                }

                override fun onFailure() {
                    binding.cvClassManagerContent.hideLoading()
                    binding.srlClassManagerReload.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvClassManagerContent.hideLoading()
                    binding.cvClassManagerContent.submitList(it.data?.dataList)
                    binding.srlClassManagerReload.isRefreshing = false
                    viewModel.resetState()
                }
            }, canShowLoadMore = binding.cvClassManagerContent.getLoadMoreState())
        }
    }

    private fun addAdapter() {
        binding.ccvClassManagerTabParent.setOnclickTabCategory {
            viewModel.classType = if (it.id == 1) CLASS_TYPE.REGULAR_TYPE else CLASS_TYPE.DEMO_TYPE
            viewModel.getClassList(isReload = true, isShowLoading = true)
        }

        binding.cvClassManagerContent.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(classManagerAdapter)
            setLoadMoreListener {
                viewModel.getClassList(isReload = false, isShowLoading = false)
            }
        }
    }

    private fun addHeader() {
        binding.srlClassManagerReload.setColorSchemeResources(R.color.primary)
        binding.ccvClassManagerTabParent.addDataList(getDataCategoryClassType())

        binding.srlClassManagerReload.setOnRefreshListener {
            binding.cvClassManagerContent.removeEmpty()
            viewModel.getClassList(isReload = true, isShowLoading = false)
        }
    }
}
