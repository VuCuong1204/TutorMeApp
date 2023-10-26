package vn.tutorme.mobile.presenter.bannerinfo.event

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.loadImage
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.EventDetailFragmentBinding
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo

@AndroidEntryPoint
class EventDetailFragment : TutorMeFragment<EventDetailFragmentBinding>(R.layout.event_detail_fragment) {

    companion object {
        const val EVENT_ID_KEY = "EVENT_ID_KEY"
    }

    private val viewModel by viewModels<EventDetailViewModel>()

    override fun onInitView() {
        super.onInitView()
        addHeader()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.eventInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    it.data?.let { value -> setContentInfo(value) }
                }
            }, canShowLoading = true)
        }
    }

    private fun addHeader() {
        binding.ivEventDetailBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }

    private fun setContentInfo(value: BannerEventInfo) {
        with(binding) {
            ivEventDetailBanner.loadImage(value.banner)
            tvEventDetailTitle.text = value.title
            tvEventDetailTime.text = value.getTimeBegin()
            tvEventDetailDuration.text = value.getCountPeopleRegister()
            tvEventDetailDescription.text = value.describe
            tvEventDetailJoin.text = value.joinInstructions
        }
    }
}
