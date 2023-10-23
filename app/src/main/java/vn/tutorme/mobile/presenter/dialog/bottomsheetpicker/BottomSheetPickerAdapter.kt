package vn.tutorme.mobile.presenter.dialog.bottomsheetpicker

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.hide
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.databinding.BottomSheetPickerItemBinding
import vn.tutorme.mobile.databinding.SelectGenderItemBinding
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.presenter.profile.model.BottomSheetPicker

class BottomSheetPickerAdapter : TutorMeAdapter() {

    companion object {
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null

    var viewType = TYPE_PICKER.DEFAULT

    override fun getLayoutResource(viewType: Int) = when (viewType) {
        1 -> R.layout.select_gender_item
        else -> R.layout.bottom_sheet_picker_item
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> =
        when (viewType) {
            1 -> GenderViewHolder(binding)
            else -> SimpleViewHolder(binding)
        }

    inner class SimpleViewHolder(binding: ViewDataBinding) : BaseVH<BottomSheetPicker>(binding) {
        private val viewBinding = binding as BottomSheetPickerItemBinding

        init {
            viewBinding.llBottomSheetPicker.setOnClickListener {
                for (i in dataList.indices) {
                    with(dataList[i] as BottomSheetPicker) {
                        if (isSelected) {
                            isSelected = false
                            notifyItemChanged(i, SELECT_PAYLOAD)
                        }
                    }
                }
                (dataList[absoluteAdapterPosition] as BottomSheetPicker).let {
                    it.isSelected = true
                    notifyItemChanged(absoluteAdapterPosition, SELECT_PAYLOAD)
                    listener?.onItemClick(it)
                }
            }
        }

        override fun onBind(data: BottomSheetPicker) {
            with(viewBinding) {
                if (viewType == TYPE_PICKER.FILTER_TIME || viewType == TYPE_PICKER.SORT) {
                    getTextPrimary(data.isSelected)
                } else {
                    tvBottomSheetPickerText.setTextColor(getAppColor(R.color.text_blue))
                }
                tvBottomSheetPickerText.text = data.title
            }
            updateSelect(data.isSelected)
        }

        override fun onBind(data: BottomSheetPicker, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                if (viewType == TYPE_PICKER.FILTER_TIME || viewType == TYPE_PICKER.SORT) {
                    getTextPrimary(data.isSelected)
                }
                updateSelect(data.isSelected)
            }
        }

        private fun updateSelect(isSelected: Boolean) {
            with(viewBinding) {
                if (isSelected) {
                    ivBottomSheetPickerTick.show()
                } else {
                    ivBottomSheetPickerTick.hide()
                }
            }
        }

        private fun getTextPrimary(isSelected: Boolean) {
            with(viewBinding) {
                return if (isSelected) {
                    tvBottomSheetPickerText.setTextColor(getAppColor(R.color.primary))
                } else {
                    tvBottomSheetPickerText.setTextColor(getAppColor(R.color.neutral_1))
                }
            }
        }
    }

    inner class GenderViewHolder(binding: ViewDataBinding) : BaseVH<BottomSheetPicker>(binding) {
        private val viewBinding = binding as SelectGenderItemBinding

        init {
            viewBinding.rbSelectGender.setOnClickListener {
                listener?.onItemClick(dataList[absoluteAdapterPosition] as BottomSheetPicker)
            }
        }

        override fun onBind(data: BottomSheetPicker) {
            when (data.optionalData as GENDER_TYPE) {
                GENDER_TYPE.MALE_TYPE -> viewBinding.rbSelectGender.text = getAppString(R.string.male)
                GENDER_TYPE.FEMALE_TYPE -> viewBinding.rbSelectGender.text = getAppString(R.string.female)
                GENDER_TYPE.OTHER -> viewBinding.rbSelectGender.text = getAppString(R.string.other)
            }
        }
    }

    interface IListener {
        fun onItemClick(bottomSheetPicker: BottomSheetPicker)
    }
}
