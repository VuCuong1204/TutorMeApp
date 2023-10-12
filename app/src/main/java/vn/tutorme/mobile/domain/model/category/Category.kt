package vn.tutorme.mobile.domain.model.category

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.presenter.classmanager.CLASS_STATE
import vn.tutorme.mobile.presenter.classmanager.CLASS_TYPE

@Parcelize
data class Category(

    var id: Int? = null,

    var name: String? = null,

    var image: Int? = null,

    var checked: Boolean? = null

) : IParcelable {
    @IgnoredOnParcel
    var type: Any? = null
}

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

fun getDataCategoryClassType(): List<Category> {
    return listOf(
        Category(
            id = 1, name = getAppString(R.string.class_regular), checked = true
        ).apply { type = CLASS_TYPE.REGULAR_TYPE },
        Category(
            id = 2, name = getAppString(R.string.class_demo), checked = false
        ).apply { type = CLASS_TYPE.DEMO_TYPE }
    )
}

fun getDataCategoryClassState(): List<Category> {
    return listOf(
        Category(
            id = 1, name = getAppString(R.string.class_happen), checked = true
        ).apply { type = CLASS_STATE.STUDYING_STATE },
        Category(
            id = 2, name = getAppString(R.string.class_learn), checked = false
        ).apply { type = CLASS_STATE.LEARNED_STATE }
    )
}
