package vn.tutorme.mobile.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseVH<DATA>(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    open fun onBind(data: DATA) {}
    open fun onBind(data: DATA, payloads: List<Any>) {}
}
