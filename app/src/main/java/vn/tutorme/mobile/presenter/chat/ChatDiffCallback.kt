package vn.tutorme.mobile.presenter.chat

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.domain.model.chat.ChatInfo

class ChatDiffCallback(
    oldList: List<Any>,
    newList: List<Any>
) : BaseDiffUtilCallback<Any>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ChatInfo
        val newItem = getNewItem(newItemPosition) as? ChatInfo

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ChatInfo
        val newItem = getNewItem(newItemPosition) as? ChatInfo

        return oldItem?.hashCode() == newItem?.hashCode()
    }
}
