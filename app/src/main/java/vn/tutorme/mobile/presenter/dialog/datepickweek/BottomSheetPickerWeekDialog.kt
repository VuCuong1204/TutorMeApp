package vn.tutorme.mobile.presenter.dialog.datepickweek

import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.BottomSheetPickerWeekDialogBinding

class BottomSheetPickerWeekDialog : TutorMeDialog<BottomSheetPickerWeekDialogBinding>(R.layout.bottom_sheet_picker_week_dialog) {

    private val bottomSheetPickerWeekAdapter by lazy { BottomSheetPickerWeekAdapter() }
    var dataList: List<DateWeekDisplay>? = null
    private var onClick: ((DateWeekDisplay) -> Unit)? = null

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = false
    }

    override fun onInitView() {
        super.onInitView()

        binding.rvBottomSheetPickerWeek.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(bottomSheetPickerWeekAdapter)
            submitList(dataList)
        }

        bottomSheetPickerWeekAdapter.listener = object : IBottomSheetPickerWeekDialog {
            override fun onItemClick(item: DateWeekDisplay) {
                onClick?.invoke(item)
                dismiss()
            }
        }
    }

    private fun setOnClick(active: ((DateWeekDisplay) -> Unit)) {
        onClick = active
    }
}
