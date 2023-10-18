package vn.tutorme.mobile.base.adapter

import vn.tutorme.mobile.R

abstract class TutorMeAdapter : BaseAdapter() {

    fun <DATA> BaseVH<DATA>.getItem(action: ((DATA) -> Unit)) {
        val item = getDataListAtPosition(absoluteAdapterPosition) as? DATA
        item?.let {
            action.invoke(it)
        }
    }
}
