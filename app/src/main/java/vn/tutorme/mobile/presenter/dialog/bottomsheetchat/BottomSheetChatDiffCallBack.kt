package vn.tutorme.mobile.presenter.dialog.bottomsheetchat

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback

class BottomSheetChatDiffCallBack(
    oldList: List<Any>,
    newList: List<Any>,
) : BaseDiffUtilCallback<Any>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ChatRoomInfoDisplay
        val newItem = getNewItem(newItemPosition) as? ChatRoomInfoDisplay

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ChatRoomInfoDisplay
        val newItem = getNewItem(newItemPosition) as? ChatRoomInfoDisplay

        return oldItem?.hashCode() == newItem?.hashCode()
    }
}
