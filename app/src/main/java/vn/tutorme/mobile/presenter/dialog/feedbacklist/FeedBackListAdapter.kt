package vn.tutorme.mobile.presenter.dialog.feedbacklist

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.databinding.FeedBackItemBinding

class FeedBackListAdapter : TutorMeAdapter() {

    override fun getLayoutResource(viewType: Int): Int = R.layout.feed_back_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return FeedBackListVH(binding as FeedBackItemBinding)
    }

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.feed_back_list_empty)

    inner class FeedBackListVH(private val binding: FeedBackItemBinding) : BaseVH<String>(binding) {
        override fun onBind(data: String) {
            super.onBind(data)
            binding.tvFeedBackContent.text = data
        }
    }
}
