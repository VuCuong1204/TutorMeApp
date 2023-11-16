package vn.tutorme.mobile.presenter.chat.user

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.UserChatItemBinding
import vn.tutorme.mobile.domain.model.chat.SingleRoomInfo

class ChatUserAdapter : TutorMeAdapter() {

    var listener: IChatUserListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.user_chat_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return ChatUserVH(binding as UserChatItemBinding)
    }

    inner class ChatUserVH(private val binding: UserChatItemBinding) : BaseVH<SingleRoomInfo>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem { listener?.onItemClick(it) }
            }
        }

        override fun onBind(data: SingleRoomInfo) {
            super.onBind(data)
            with(binding) {
                ivUserChatAvatar.loadUser(data.avatarReceiver)
                tvUserChatContent.text = data.nameReceiver
            }
        }
    }

    interface IChatUserListener {
        fun onItemClick(item: SingleRoomInfo)
    }
}
