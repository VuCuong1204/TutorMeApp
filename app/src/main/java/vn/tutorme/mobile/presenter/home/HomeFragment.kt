package vn.tutorme.mobile.presenter.home

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.HomeFragmentBinding

class HomeFragment : TutorMeFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()
    private val homeAdapter by lazy { HomeAdapter() }

    override fun onInitView() {
        super.onInitView()
        addAdapter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        coroutinesLaunch(viewModel.homeState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    binding.cvHomeRoot.submitList(it.data?.dataList)
                }
            })
        }
    }

    private fun addAdapter() {
        binding.cvHomeRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(homeAdapter)
        }
    }
}
