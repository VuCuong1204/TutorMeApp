package vn.tutorme.mobile.presenter.home.banner

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.loadImage
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.BannerItemBinding
import vn.tutorme.mobile.domain.model.banner.Banner

class BannerHomeAdapter : TutorMeAdapter() {

    var listener: IBannerHomeAdapter? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.banner_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return BannerHomeVH(binding as BannerItemBinding)
    }

    inner class BannerHomeVH(private val binding: BannerItemBinding) : BaseVH<Banner>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem {
                    listener?.onItemClick(it)
                }
            }
        }

        override fun onBind(data: Banner) {
            super.onBind(data)

            binding.ivBannerRoot.loadImage(data.link)
        }
    }

    interface IBannerHomeAdapter {
        fun onItemClick(item: Banner
        )
    }
}
