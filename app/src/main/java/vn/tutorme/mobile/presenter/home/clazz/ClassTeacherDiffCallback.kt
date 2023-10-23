package vn.tutorme.mobile.presenter.home.clazz

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.domain.model.clazz.ClassInfo

class ClassTeacherDiffCallback(
    oldList: List<Any>,
    newList: List<Any>
) : BaseDiffUtilCallback<Any>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ClassInfo
        val newItem = getNewItem(newItemPosition) as? ClassInfo

        return oldItem?.classId == newItem?.classId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition) as? ClassInfo
        val newItem = getNewItem(newItemPosition) as? ClassInfo

        return false
    }
}
