package vn.tutorme.mobile.domain.model.category

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class Category(

    var id: Int? = null,

    var name: String? = null,

    var image: Int? = null,

    var checked: Boolean? = null
) : IParcelable

fun getDataCategoryClass(): List<Category> {
    return listOf(
        Category(
            id = 1, name = getAppString(R.string.waiting_confirm), checked = true
        ),
        Category(
            id = 2, name = getAppString(R.string.confirmed), checked = false
        ),
        Category(
            id = 3, name = getAppString(R.string.out_of_date), checked = false
        )
    )
}
