package vn.tutorme.mobile.presenter.widget.categoryclass

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.domain.model.category.Category

class CategoryClassDiffCallback(
    oldData: List<Any>,
    newData: List<Any>
) : BaseDiffUtilCallback<Any>(oldData, newData) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? Category
        val newItem = getNewItem(newItemPosition) as? Category

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? Category
        val newItem = getNewItem(newItemPosition) as? Category

        return oldItem?.checked == newItem?.checked
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = getOldItem(oldItemPosition) as? Category
        val newItem = getNewItem(newItemPosition) as? Category

        val list = mutableListOf<Any>()
        if (oldItem?.checked != newItem?.checked) list.add(CHECKED_PAYLOAD)

        return list.ifEmpty { null }
    }
}

const val CHECKED_PAYLOAD = "CHECKED_PAYLOAD"
