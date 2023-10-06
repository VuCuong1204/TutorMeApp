package vn.tutorme.mobile.presenter.home

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.HomeFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE

@AndroidEntryPoint
class HomeFragment : TutorMeFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()
    private val homeAdapter by lazy { HomeAdapter() }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        coroutinesLaunch(viewModel.homeState) {
            handleUiState(it, object : IViewListener {

                override fun onLoading() {
                    binding.cvHomeRoot.showLoading()
                }

                override fun onFailure() {
                    binding.cvHomeRoot.hideLoading()
                    binding.srlHomeRoot.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvHomeRoot.submitList(it.data?.dataList)
                    viewModel.resetState()
                    binding.cvHomeRoot.hideLoading()
                    binding.srlHomeRoot.isRefreshing = false
                }
            })
        }
    }

    override fun onBackPressByFragment() {
        exitScreen()
    }

    private fun addHeader() {
        binding.srlHomeRoot.setColorSchemeResources(R.color.primary)
    }

    private fun addAdapter() {
        binding.cvHomeRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(homeAdapter)
        }

        binding.srlHomeRoot.setOnRefreshListener {
            if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
                viewModel.getHomeStudent(true)
            } else {
                viewModel.getHomeTeacher(true)
            }
        }
    }
}
