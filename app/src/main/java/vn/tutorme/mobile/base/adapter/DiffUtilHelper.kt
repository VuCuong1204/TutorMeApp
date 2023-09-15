package vn.tutorme.mobile.base.adapter

import androidx.recyclerview.widget.DiffUtil

open class BaseDiffUtilCallback<T : Any>(
    private val oldData: List<T>,
    private val newData: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false

    fun getOldItem(position: Int): T = oldData[position]

    fun getNewItem(position: Int): T = newData[position]
}
