package vn.tutorme.mobile.presenter.widget.categoryclass

import vn.tutorme.mobile.domain.model.category.Category

interface IListenerCategory {
    fun onItemClick(index: Int, item: Category)
}
