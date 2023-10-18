package vn.tutorme.mobile.base.adapter

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension.COLUMN_IN_ROW_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.databinding.BaseEmptyDefaultItemBinding
import vn.tutorme.mobile.databinding.BaseLoadMoreDefaultItemBinding

abstract class BaseAdapter : RecyclerView.Adapter<BaseVH<Any>>() {

    companion object {
        const val LAYOUT_INVALID = -1
        const val EMPTY_VIEW_TYPE = 0
        const val LOAD_MORE_VIEW_TYPE = 1
        const val LOADING_VIEW_TYPE = 2
        const val NORMAL_VIEW_TYPE = 3
    }

    var dataList: MutableList<Any> = mutableListOf()
        private set

    abstract fun getLayoutResource(viewType: Int): Int

    open fun getColumnInRow(viewType: Int): Int = COLUMN_IN_ROW_DEFAULT

    /**
     * Sử dụng khi dùng group adapter
     */
    open fun getColumnInRowList(): List<Int> = listOf()

    abstract fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>?

    open fun getLayoutEmpty() = Empty()

    open fun getLayoutLoadMore() = LoadMore()

    open fun getLayoutLoading() = R.layout.base_loading_default_item

    open fun getLayoutEmptyDefault(): Int = R.layout.base_empty_default_item

    open fun getLayoutLoadMoreDefault() = R.layout.base_load_more_default_item

    open fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return BaseDiffUtilCallback(oldList, newList)
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is Empty -> {
                EMPTY_VIEW_TYPE
            }

            is LoadMore -> {
                LOAD_MORE_VIEW_TYPE
            }

            is Loading -> {
                LOADING_VIEW_TYPE
            }

            else -> {
                NORMAL_VIEW_TYPE
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseVH<Any>, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun onBindViewHolder(holder: BaseVH<Any>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            holder.onBind(dataList[position])
        } else {
            holder.onBind(dataList[position], payloads)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<Any> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding
        val layoutId: Int
        return when (viewType) {
            EMPTY_VIEW_TYPE -> {
                layoutId = if (getLayoutEmpty().layoutResource != null) {
                    getLayoutEmpty().layoutResource!!
                } else {
                    getLayoutEmptyDefault()
                }
                binding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
                EmptyVH(binding) as BaseVH<Any>
            }

            LOAD_MORE_VIEW_TYPE -> {
                layoutId = if (getLayoutLoadMore().layoutResource != null) {
                    getLayoutLoadMore().layoutResource!!
                } else {
                    getLayoutLoadMoreDefault()
                }
                binding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
                LoadMoreVH(binding) as BaseVH<Any>
            }

            LOADING_VIEW_TYPE -> {
                binding = DataBindingUtil.inflate(layoutInflater, getLayoutLoading(), parent, false)
                LoadingVH(binding) as BaseVH<Any>
            }

            else -> {
                if (getLayoutResource(viewType) != LAYOUT_INVALID) {
                    binding = DataBindingUtil.inflate(layoutInflater, getLayoutResource(viewType), parent, false)
                    onCreateViewHolder(viewType, binding) as BaseVH<Any>
                } else {
                    throw IllegalArgumentException("Can not find layout for type: $viewType")
                }
            }
        }
    }

    fun getDataListAtPosition(position: Int): Any = dataList[position]

    fun addLoading() {
        dataList.add(Loading())
        notifyItemInserted(dataList.size)
    }

    fun removeLoading() {
        if (dataList.isNotEmpty()) {
            val loading = dataList.find {
                it is Loading
            }

            val position = dataList.indexOfFirst {
                it == loading
            }

            if (position >= 0) {
                dataList.remove(loading)
                notifyItemRemoved(position)
            }
        }
    }

    fun addEmpty() {
        dataList.clear()
        dataList.add(getLayoutEmpty())
        notifyItemInserted(0)
    }

    fun addLoadMore() {
        dataList.add(getLayoutLoadMore())
        notifyItemInserted(dataList.size)
    }

    fun removeEmpty() {
        if (dataList.isNotEmpty()) {
            val lastIndex = dataList.lastIndex
            if (lastIndex >= 0) {
                if (dataList[lastIndex] is Empty) {
                    dataList.removeLast()
                    notifyItemRemoved(lastIndex)
                }
            }
        }
    }

    fun clearData() {
        dataList.clear()
    }

    fun removeLoadMore() {
        if (dataList.isNotEmpty()) {
            val position = dataList.lastIndex

            if (getDataListAtPosition(position) is LoadMore) {
                dataList.removeLast()
                notifyItemRemoved(position)
            }
        }
    }

    fun setDataList(newItems: List<Any>?) {
        removeLoading()
        val newList = newItems?.toMutableList()
        if (newList != null) {
            val callback = getDiffUtil(dataList, newList)
            val diffResult = DiffUtil.calculateDiff(callback)
            this.dataList = newList
            diffResult.dispatchUpdatesTo(this)
        }
    }

    inner class EmptyVH(binding: ViewDataBinding) : BaseVH<Empty>(binding) {
        private val viewDataBinding = binding as? BaseEmptyDefaultItemBinding
        override fun onBind(data: Empty) {
            super.onBind(data)
            if (viewDataBinding != null) {
                viewDataBinding.ivBaseEmptyDefaultItmIcon.setImageDrawable(data.imageEmpty)
                viewDataBinding.tvBaseEmptyDefaultItmMessage.text = data.message
            }
        }
    }

    inner class LoadMoreVH(binding: ViewDataBinding) : BaseVH<LoadMore>(binding) {
        private val viewDataBinding = binding as? BaseLoadMoreDefaultItemBinding
        override fun onBind(data: LoadMore) {
            super.onBind(data)
            if (viewDataBinding != null) {
                viewDataBinding.tvBaseLoadMoreDefaultItmMessage.apply {
                    text = data.message
                    setTextColor(data.messageColor)
                }

                viewDataBinding.pbBaseLoadMoreDefaultItmLoading
                    .indeterminateDrawable
                    .setColorFilter(data.progressBarColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    inner class LoadingVH(binding: ViewDataBinding) : BaseVH<Loading>(binding)

    class Loading

    class LoadMore(
        val message: String = STRING_DEFAULT,
        val messageColor: Int = getAppColor(R.color.gray),
        val progressBarColor: Int = getAppColor(R.color.gray),
        val layoutResource: Int? = null
    )

    class Empty(
        val message: String = STRING_DEFAULT,
        val messageColor: Int = getAppColor(R.color.gray),
        val imageEmpty: Drawable? = getAppDrawable(R.drawable.default_photo),
        val layoutResource: Int? = null
    )
}
