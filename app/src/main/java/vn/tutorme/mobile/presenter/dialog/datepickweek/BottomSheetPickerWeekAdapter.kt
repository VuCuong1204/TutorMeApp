package vn.tutorme.mobile.presenter.dialog.datepickweek

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDimensionPixel
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.DateWeekItemBinding

class BottomSheetPickerWeekAdapter : TutorMeAdapter() {

    var listener: IBottomSheetPickerWeekDialog? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.date_week_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return BottomSheetPickerWeekVH(binding as DateWeekItemBinding)
    }

    inner class BottomSheetPickerWeekVH(private val binding: DateWeekItemBinding) : BaseVH<DateWeekDisplay>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem { dateWeekDisplay ->
                    listener?.onItemClick(dateWeekDisplay)
                }
            }
        }

        override fun onBind(data: DateWeekDisplay) {
            super.onBind(data)
            with(binding) {
                if (data.selected == true) {
                    tvDateWeekTime.textSize = getAppDimensionPixel(R.dimen.fbase_text_size_22).toFloat()
                    tvDateWeekTime.setTextColor(getAppColor(R.color.primary))
                    tvDateWeekTime.setBackgroundColor(getAppColor(R.color.purple))
                } else {
                    tvDateWeekTime.textSize = getAppDimensionPixel(R.dimen.fbase_text_size_16).toFloat()
                    tvDateWeekTime.setTextColor(getAppColor(R.color.black))
                    tvDateWeekTime.setBackgroundColor(getAppColor(R.color.white))
                }
            }
        }
    }
}
