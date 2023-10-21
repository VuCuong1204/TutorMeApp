package vn.tutorme.mobile.presenter.profile

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.ProfileItemBinding
import vn.tutorme.mobile.domain.model.profile.ProfileInfo

class ProfileAdapter : TutorMeAdapter() {

    var listener: IProfileListener? = null

    override fun getColumnInRow(viewType: Int): Int = 3

    override fun getLayoutResource(viewType: Int): Int = R.layout.profile_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return ProfileVH(binding as ProfileItemBinding)
    }

    inner class ProfileVH(private val binding: ProfileItemBinding) : BaseVH<ProfileInfo>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem {
                    listener?.onItemClick(it)
                }
            }
        }

        override fun onBind(data: ProfileInfo) {
            super.onBind(data)

            data.avatar?.let { binding.ivProfileAvatar.setImageResource(it) }
            binding.tvProfileName.text = data.name
        }
    }
}
