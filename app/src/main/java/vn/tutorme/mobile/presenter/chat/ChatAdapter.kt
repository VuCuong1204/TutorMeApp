package vn.tutorme.mobile.presenter.chat

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.databinding.ChatReceiverItemBinding
import vn.tutorme.mobile.databinding.ChatSendItemBinding
import vn.tutorme.mobile.domain.model.chat.ChatInfo

class ChatAdapter : TutorMeAdapter() {

    companion object {
        const val SENDER_TYPE = 10
        const val RECEIVER_TYPE = 11
    }

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            SENDER_TYPE -> R.layout.chat_send_item
            RECEIVER_TYPE -> R.layout.chat_receiver_item
            else -> LAYOUT_INVALID
        }
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return ChatDiffCallback(oldList, newList)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getDataListAtPosition(position) as ChatInfo
        return when (item.sendId) {
            AppPreferences.userInfo?.userId -> SENDER_TYPE
            else -> RECEIVER_TYPE
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            SENDER_TYPE -> ChatSendVH(binding as ChatSendItemBinding)
            RECEIVER_TYPE -> ChatReceiverVH(binding as ChatReceiverItemBinding)
            else -> null
        }
    }

    inner class ChatSendVH(private val binding: ChatSendItemBinding) : BaseVH<ChatInfo>(binding) {
        override fun onBind(data: ChatInfo) {
            super.onBind(data)
            with(binding) {
                ivChatSenderAvatar.loadUser(data.url)
                tvChatSenderContent.text = data.content
            }
        }
    }

    inner class ChatReceiverVH(private val binding: ChatReceiverItemBinding) : BaseVH<ChatInfo>(binding) {
        override fun onBind(data: ChatInfo) {
            super.onBind(data)
            with(binding) {
                ivChatReceiverAvatar.loadUser(data.url)
                tvChatReceiverContent.text = data.content
            }
        }
    }
}
