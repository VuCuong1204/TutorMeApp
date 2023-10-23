package vn.tutorme.mobile.presenter.profile.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
class BottomSheetPicker(

    var id: String? = null,

    var title: String? = null,

    var isSelected: Boolean = false

) : IParcelable {

    @IgnoredOnParcel
    var optionalData: Any? = null

    fun getBackground(): Int {
        return if (isSelected) {
            R.drawable.shape_rectange_blue_bg_corner_6
        } else {
            R.drawable.shape_rectange_white_bg_black_stroke_corner_6
        }
    }

    fun getTextColor(): Int {
        return if (isSelected) {
            R.color.white
        } else {
            R.color.black
        }
    }
}
