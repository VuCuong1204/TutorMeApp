package vn.tutorme.mobile.presenter.widget.collection

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.base.adapter.BaseAdapter
import vn.tutorme.mobile.base.adapter.BaseAdapter.Companion.EMPTY_VIEW_TYPE
import vn.tutorme.mobile.base.adapter.BaseAdapter.Companion.LOADING_VIEW_TYPE
import vn.tutorme.mobile.base.adapter.BaseAdapter.Companion.LOAD_MORE_VIEW_TYPE

class CollectionView(
    context: Context,
    attrs: AttributeSet?
) : RecyclerView(context, attrs) {

    private var layoutManager: LayoutManager? = null
    private var baseAdapter: BaseAdapter? = null
    private var scrollListener: BaseRecyclerviewScrollListener? = null
    private var activeLoadStatus: Boolean = false
    private var isLoadMore: Boolean = false
    private var orientation: LAYOUT_MANAGER? = LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL
    private var columnMax = 1

    init {
        this.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            setItemViewCacheSize(50)
        }
    }

    fun setBaseAdapter(adapter: BaseAdapter) {
        baseAdapter = adapter
        this.adapter = adapter
    }

    fun setBaseGroupAdapter(adapter: RecyclerView.Adapter<*>) {
        this.adapter = adapter
    }

    fun setBaseLayoutManager(layout: LAYOUT_MANAGER, spanCount: Int = 2) {
        orientation = layout
        when (layout) {
            LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL -> {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                setLayoutManager(layoutManager)
            }

            LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL -> {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setLayoutManager(layoutManager)
            }

            LAYOUT_MANAGER.GRIDLAYOUT_HORIZONTAL -> {
                layoutManager = GridLayoutManager(
                    context,
                    spanCount,
                    GridLayoutManager.HORIZONTAL,
                    false)

                setLayoutManager(layoutManager)
            }

            LAYOUT_MANAGER.GRIDLAYOUT_VERTICAL -> {
                columnMax = getMaxCountColumn(spanCount)

                layoutManager = GridLayoutManager(
                    context,
                    columnMax,
                    GridLayoutManager.VERTICAL,
                    false
                )

                getSpanSizeLookup()
                setLayoutManager(layoutManager)
            }

            LAYOUT_MANAGER.STAGGERED_VERTICAL -> {
                layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
                setLayoutManager(layoutManager)
            }
        }
    }

    fun submitList(newList: List<Any>? = null) {
        if (scrollListener?.isLoadMore == true) {
            hideLoadMore()
        }
        if (newList.isNullOrEmpty()) {
            baseAdapter?.addEmpty()
        } else {
            baseAdapter?.setDataList(newList)
            scrollListener?.isLoadMore = false
        }
    }

    fun setLoadMoreListener(action: (() -> Unit)? = null) {
        scrollListener = object : BaseRecyclerviewScrollListener(layoutManager, orientation) {
            override fun onLoadMore() {
                post {
                    isLoadMore = true
                    showLoadMore()
                    baseAdapter?.itemCount?.let { smoothScrollToPosition(it) }
                    action?.invoke()
                }
            }
        }

        scrollListener?.let {
            this.addOnScrollListener(it)
        }
    }

    fun getLoadMoreState(): Boolean {
        return scrollListener?.isLoadMore ?: false
    }

    fun showLoadMore() {
        baseAdapter?.addLoadMore()
    }

    fun hideLoadMore() {
        baseAdapter?.removeLoadMore()
        scrollListener?.isLoadMore = false
    }

    fun showLoading() {
        hideLoading()
        baseAdapter?.addLoading()
    }

    fun hideLoading() {
        baseAdapter?.removeLoading()
    }

    fun removeEmpty() {
        baseAdapter?.removeEmpty()
    }

    fun clearData() {
        baseAdapter?.clearData()
    }

    private fun getSpanSizeLookup() {
        if (layoutManager is GridLayoutManager) {
            (layoutManager as GridLayoutManager).spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    when (baseAdapter?.getItemViewType(position)) {
                        EMPTY_VIEW_TYPE, LOADING_VIEW_TYPE, LOAD_MORE_VIEW_TYPE -> {
                            columnMax
                        }

                        else -> {
                            val value = baseAdapter?.getColumnInRow(baseAdapter?.getItemViewType(position)
                                ?: EMPTY_VIEW_TYPE)

                            return columnMax / (value ?: 1)
                        }
                    }
                    return columnMax
                }
            }
        }
    }

    private fun getItemMaxInRow(position: Int): Int {
        when (baseAdapter?.getItemViewType(position)) {
            EMPTY_VIEW_TYPE, LOADING_VIEW_TYPE, LOAD_MORE_VIEW_TYPE -> {
                columnMax
            }

            else -> {
                val value = baseAdapter?.getColumnInRow(baseAdapter?.getItemViewType(position)
                    ?: EMPTY_VIEW_TYPE)

                return columnMax / (value ?: 1)
            }
        }

        return columnMax
    }

    private fun getMaxCountColumn(maxItem: Int = 3): Int {
        var number = 1
        for (i in 1..maxItem) {
            number *= i
        }

        return number
    }
}
