package vn.tutorme.mobile.presenter.dialog.feedbacklist

import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.FeedBackListDialogBinding

class FeedBackListDialog : TutorMeDialog<FeedBackListDialogBinding>(R.layout.feed_back_list_dialog) {

    private val feedBackListAdapter by lazy { FeedBackListAdapter() }
    var dataList = listOf<String>()

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = true
    }

    override fun getBackgroundId(): Int = R.id.flFeedBackListRoot

    override fun onInitView() {
        super.onInitView()

        binding.ivFeedbackListClose.setOnSafeClick { dismiss() }
        binding.cvFeedbackMessage.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(feedBackListAdapter)
            submitList(dataList)
        }
    }
}
