package vn.tutorme.mobile.presenter.dialog.select

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.presenter.dialog.select.model.ClassDetailDisplay

class SelectCollectionDiffCallback(
    oldData: List<Any>,
    newData: List<Any>
) : BaseDiffUtilCallback<Any>(oldData, newData) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ClassDetailDisplay
        val newItem = getNewItem(newItemPosition) as? ClassDetailDisplay

        return oldItem?.classInfo?.classId == newItem?.classInfo?.classId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContent = getOldItem(oldItemPosition) as? ClassDetailDisplay
        val newContent = getNewItem(newItemPosition) as? ClassDetailDisplay

        return (oldContent?.isSelected == newContent?.isSelected)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = getOldItem(oldItemPosition) as? ClassDetailDisplay
        val newItem = getNewItem(newItemPosition) as? ClassDetailDisplay

        val list = mutableListOf<Any>()
        if (oldItem?.isSelected != newItem?.isSelected) {
            list.add(TICK_COLLECTION_PAYLOAD)
        }

        return list.ifEmpty { null }
    }
}

const val TICK_COLLECTION_PAYLOAD = "TICK_COLLECTION_PAYLOAD"
