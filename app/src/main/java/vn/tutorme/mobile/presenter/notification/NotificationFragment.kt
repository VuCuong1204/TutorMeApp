package vn.tutorme.mobile.presenter.notification

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.NotificationFragmentBinding
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.presenter.home.HomeFragment

@AndroidEntryPoint
class NotificationFragment : TutorMeFragment<NotificationFragmentBinding>(R.layout.notification_fragment) {

    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val notificationAdapter by lazy { NotificationAdapter() }

    override fun onInitView() {
        super.onInitView()
        addListener()
        addHeader()
        addAdapter()
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(notificationViewModel.notificationState) {
            handleUiState(it, object : IViewListener {

                override fun onLoading() {
                    binding.cvNotificationRoot.showLoading()
                }

                override fun onFailure() {
                    binding.cvNotificationRoot.hideLoading()
                    binding.srlNotificationReload.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvNotificationRoot.hideLoading()
                    binding.srlNotificationReload.isRefreshing = false
                    binding.cvNotificationRoot.submitList(it.data?.dataList)
                    notificationViewModel.resetNotificationState()
                }
            }, canShowLoadMore = binding.cvNotificationRoot.getLoadMoreState())
        }
    }

    private fun addListener() {
        notificationAdapter.listener = object : INotificationListener {
            override fun onReadClick(item: NotificationInfo) {
                //     TODO("Not yet implemented")
            }

            override fun onDeleteClick(id: String) {
                //     TODO("Not yet implemented")
            }
        }
    }

    private fun addAdapter() {
        binding.cvNotificationRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(notificationAdapter)
            setLoadMoreListener {
                notificationViewModel.getNotificationInfoList(isReload = false, isShowLoading = false)
            }
        }

        binding.srlNotificationReload.setOnRefreshListener {
            notificationViewModel.getNotificationInfoList(isReload = true, isShowLoading = false)
        }
    }

    private fun addHeader() {}

    private fun removeListener() {
        notificationAdapter.listener = null
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }
}
