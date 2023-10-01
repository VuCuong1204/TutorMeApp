package vn.tutorme.mobile.presenter.widget.collection

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.base.extension.Extension

abstract class BaseRecyclerviewScrollListener(
    private val layoutManager: LayoutManager?,
    private val orientation: LAYOUT_MANAGER?
) : RecyclerView.OnScrollListener() {

    private var lastCurrentPosition: Int = Extension.INT_DEFAULT
    private var totalItemCount = 0
    var isLoadMore: Boolean = false
    var isLastPage: Boolean = false
    var isScrollLength: Int = 0

    open fun onLoadMore() {}

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (orientation == LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL ||
            orientation == LAYOUT_MANAGER.GRIDLAYOUT_VERTICAL ||
            orientation == LAYOUT_MANAGER.STAGGERED_VERTICAL
        ) {
            isScrollLength = dy
        } else if (
            orientation == LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL ||
            orientation == LAYOUT_MANAGER.GRIDLAYOUT_HORIZONTAL
        ) {
            isScrollLength = dx
        }

        if (isLoadMore) return

        totalItemCount = layoutManager?.itemCount ?: 0
        if (isScrollLength > 0) {
            when (layoutManager) {

                is GridLayoutManager -> {
                    lastCurrentPosition = layoutManager.findLastVisibleItemPosition()
                }

                is LinearLayoutManager -> {
                    lastCurrentPosition = layoutManager.findLastVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                    lastCurrentPosition = getLastVisibleItem(lastVisibleItemPositions)
                }
            }

            if (!isLoadMore && totalItemCount > 0 && lastCurrentPosition == (totalItemCount - 1)) {
                isLoadMore = true
                onLoadMore()
            }
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = lastVisibleItemPositions[0]

        for (i in lastVisibleItemPositions.indices) {
            if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }

        return maxSize
    }
}
