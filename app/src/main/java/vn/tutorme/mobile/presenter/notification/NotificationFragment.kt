package vn.tutorme.mobile.presenter.notification

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.CountNotifyEvent
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.NotifyDetailResultEvent
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.NotificationFragmentBinding
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.dialog.ConfirmDialog
import vn.tutorme.mobile.presenter.home.HomeFragment
import vn.tutorme.mobile.presenter.notification.detail.NotificationDetailFragment

@AndroidEntryPoint
class NotificationFragment : TutorMeFragment<NotificationFragmentBinding>(R.layout.notification_fragment) {

    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val notificationAdapter by lazy { NotificationAdapter() }
    private var bottomSheetConfirmDialog: BottomSheetConfirmDialog? = null
    private var isReadNotiy = false

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

    override fun onStart() {
        super.onStart()
        EventBusManager.instance?.register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBusManager.instance?.unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: IEvent) {
        super.onEvent(event)
        when (event) {
            is NotifyDetailResultEvent -> {
                notificationViewModel.changeReadState(event.notifyId)
                isReadNotiy = true
                EventBusManager.instance?.removeSticky(event)
            }
        }
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
                    if (isReadNotiy) {
                        EventBusManager.instance?.postPending(CountNotifyEvent())
                        isReadNotiy = false
                    }
                }
            }, canShowLoadMore = binding.cvNotificationRoot.getLoadMoreState())
        }
    }

    private fun addListener() {
        notificationAdapter.listener = object : INotificationListener {
            override fun onReadClick(item: NotificationInfo) {
                replaceFragment(NotificationDetailFragment(), bundleOf(
                    NotificationDetailFragment.OBJECT_NOTIFY_KEY to item
                ))
            }

            override fun onDeleteClick(id: Int) {
                showDeleteNotifyDialog(id)
                isReadNotiy = true
            }
        }
    }

    private fun addAdapter() {
        binding.cvNotificationRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(notificationAdapter)
        }

        binding.srlNotificationReload.setOnRefreshListener {
            binding.cvNotificationRoot.removeEmpty()
            notificationViewModel.getNotificationInfoList(isReload = true, isShowLoading = false)
        }
    }

    private fun addHeader() {
        binding.srlNotificationReload.setColorSchemeResources(R.color.primary)
        binding.tvNotificationRead.setOnSafeClick {
            showConfirmDialog()
        }
    }

    private fun showConfirmDialog() {
        ConfirmDialog().apply {
            eventLeftClick {
                notificationViewModel.readAllNotification()
                isReadNotiy = true
            }
        }.show(childFragmentManager, ConfirmDialog::class.java.simpleName)
    }

    private fun showDeleteNotifyDialog(notifyId: Int) {
        bottomSheetConfirmDialog = BottomSheetConfirmDialog().apply {
            title = getAppString(R.string.confirm_second)
            content = getAppString(R.string.delete_notify)
            textLeft = getAppString(R.string.confirm)
            avatar = getAppDrawable(R.drawable.ic_bell_read_all)
            textRight = getAppString(R.string.cancel)
            bgTextLeft = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
            bgTextRight = getAppDrawable(R.drawable.ripple_bg_white_corner_16_stroke_1)
            clTextLeft = getAppColor(R.color.white)
            clTextRight = getAppColor(R.color.text1)
            eventLeftClick {
                notificationViewModel.removeNotification(notifyId)
            }
        }

        bottomSheetConfirmDialog?.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }

    private fun removeListener() {
        notificationAdapter.listener = null
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }
}
