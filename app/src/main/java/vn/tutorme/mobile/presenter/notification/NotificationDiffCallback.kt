package vn.tutorme.mobile.presenter.notification

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.domain.model.notification.NotificationInfo

class NotificationDiffCallback(
    oldList: List<Any>,
    newList: List<Any>
) : BaseDiffUtilCallback<Any>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? NotificationInfo
        val newItem = getNewItem(newItemPosition) as? NotificationInfo

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? NotificationInfo
        val newItem = getNewItem(newItemPosition) as? NotificationInfo

        return oldItem?.hashCode() == newItem?.hashCode()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val list = mutableListOf<Any>()

        val oldItem = getOldItem(oldItemPosition) as? NotificationInfo
        val newItem = getNewItem(newItemPosition) as? NotificationInfo

        if (oldItem?.notifyType != newItem?.notifyType) {
            list.add(READ_STATE_PAYLOAD)
        } else if (oldItem?.isLastIndex != newItem?.isLastIndex) {
            list.add(POSITION_LAST_STATE_PAYLOAD)
        }

        return list.ifEmpty { null }
    }
}

const val READ_STATE_PAYLOAD = "READ_STATE_PAYLOAD"
const val POSITION_LAST_STATE_PAYLOAD = "POSITION_LAST_STATE_PAYLOAD"
