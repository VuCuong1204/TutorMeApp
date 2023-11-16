package vn.tutorme.mobile.presenter.classmanager

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassChildFragmentBinding
import vn.tutorme.mobile.presenter.classinfo.ClassInfoFragment
import vn.tutorme.mobile.presenter.home.HomeFragment

@AndroidEntryPoint
class ClassChildFragment : TutorMeFragment<ClassChildFragmentBinding>(R.layout.class_child_fragment) {

    private val viewModel by viewModels<ClassChildViewModel>()
    private val classChildAdapter by lazy { ClassChildAdapter() }
    var classType: CLASS_TYPE = CLASS_TYPE.REGULAR_TYPE

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.classType = classType
        viewModel.getClassList()
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
                    binding.cvClassChildContent.clearData()
                    binding.cvClassChildContent.showLoading()
                }

                override fun onFailure() {
                    binding.cvClassChildContent.hideLoading()
                    binding.srlClassChildReload.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvClassChildContent.hideLoading()
                    binding.cvClassChildContent.submitList(it.data?.dataList)
                    binding.srlClassChildReload.isRefreshing = false
                    viewModel.resetState()
                }
            }, canShowLoadMore = binding.cvClassChildContent.getLoadMoreState())
        }
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }

    private fun addAdapter() {
        binding.cvClassChildContent.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(classChildAdapter)
        }

        addListener()
    }

    private fun addListener() {
        classChildAdapter.listener = object : IClassManagerListener {
            override fun onItemClick(classId: String) {
                replaceFragment(
                    fragment = ClassInfoFragment(),
                    bundle = bundleOf(ClassInfoFragment.CLASS_ID_KEY to classId),
                    screenAnim = SlideAnimation()
                )
            }
        }
    }

    private fun removeListener() {
        classChildAdapter.listener = null
    }

    private fun addHeader() {
        binding.srlClassChildReload.setColorSchemeResources(R.color.primary)
        binding.srlClassChildReload.setOnRefreshListener {
            binding.cvClassChildContent.removeEmpty()
            viewModel.getClassList(isReload = true, isShowLoading = false)
        }
    }
}
