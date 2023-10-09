package vn.tutorme.mobile.presenter.widget.categoryclass

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.R
import vn.tutorme.mobile.domain.model.category.Category
import vn.tutorme.mobile.presenter.widget.collection.CollectionView

class CategoryClassView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private lateinit var rvCategory: CollectionView
    private val categoryClassAdapter by lazy {
        CategoryClassAdapter()
    }

    var onClick: ((item: Category) -> Unit)? = null
    var dataList: List<Category>? = null
    var currentTab: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.category_class_view_layout, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        rvCategory = findViewById(R.id.rvCategoryStoreRoot)
        addListener()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()

        return ViewState(parcelable, currentTab)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val viewState = state as? ViewState
        super.onRestoreInstanceState(viewState?.savedState ?: state)
        currentTab = viewState?.tab ?: 0
        viewState?.tab?.let { setStatusCategory(it) }
    }

    fun setOnclickTabCategory(action: (Category) -> Unit) {
        onClick = action
    }

    fun getCurrentState(): Category? {
        return dataList?.get(currentTab)
    }

    fun setBuilderItem(
        builder: CategoryClassAdapter.Builder? = null
    ) {
        categoryClassAdapter.builder = builder
    }

    private fun addListener() {
        categoryClassAdapter.listener = object : IListenerCategory {
            override fun onItemClick(index: Int, item: Category) {
                setStatusCategory(index)
                rvCategory.smoothScrollToPosition(index)
                onClick?.invoke(item)
            }
        }
    }

    fun addDataList(list: List<Category>) {
        dataList = list.toList()
        rvCategory.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.GRIDLAYOUT_VERTICAL, 3)
            setBaseAdapter(categoryClassAdapter)
            submitList(list)
        }
    }

    fun setStatusCategory(newIndex: Int) {

        val newList = dataList?.toMutableList()

        val oldIndex = newList?.indexOfFirst {
            it.checked == true
        }

        val oldItem = oldIndex?.let {
            newList[it].copy(
                checked = false
            )
        }

        if (oldIndex != null && oldItem != null) {
            newList[oldIndex] = oldItem
        }

        val newItem = newList?.get(newIndex)?.copy(
            checked = true
        )

        if (newItem != null) {
            newList[newIndex] = newItem
        }

        currentTab = newIndex
        rvCategory.submitList(newList)
    }

    private fun initView(attrs: AttributeSet) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CategoryClassView, 0, 0)

        ta.recycle()
    }

    class ViewState(val savedState: Parcelable?, val tab: Int?) : BaseSavedState(savedState), Parcelable
}
