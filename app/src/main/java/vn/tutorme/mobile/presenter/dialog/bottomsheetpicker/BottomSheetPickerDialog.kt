package vn.tutorme.mobile.presenter.dialog.bottomsheetpicker

import android.content.Context
import androidx.fragment.app.FragmentManager
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.BaseBindingDialog
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.BottomSheetPickerDialogBinding
import vn.tutorme.mobile.presenter.profile.model.BottomSheetPicker
import vn.tutorme.mobile.presenter.widget.collection.layoutmanager.MaxItemLayoutManager

class BottomSheetPickerDialog constructor(
    var lstPickers: List<BottomSheetPicker> = listOf(),
    var chooseItemListener: ((BottomSheetPicker) -> Unit)? = null,
    var title: String? = null,
    var hint: String? = null,
    var ratioDialogHeight: Float = DEFAULT_RATIO_DIALOG_HEIGHT,
    var visibleItem: Int = MAX_LIST_ITEM,
    var viewTypeDialog: TYPE_PICKER = TYPE_PICKER.DEFAULT,
    var listener: IListener?
) : BaseBindingDialog<BottomSheetPickerDialogBinding>(R.layout.bottom_sheet_picker_dialog) {

    companion object {
        private const val MAX_LIST_ITEM = 5
        const val DEFAULT_RATIO_DIALOG_HEIGHT = 0.5f
    }

    private var bottomSheetPickerAdapter: BottomSheetPickerAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (bottomSheetPickerAdapter == null) {
            bottomSheetPickerAdapter = BottomSheetPickerAdapter()
        }
    }

    var builder: Builder? = null
    var values: BottomSheetPicker? = null

    override fun getBackgroundId(): Int = R.id.flBottomSheetPickerDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.BOTTOM
        isFullWidth = true
        isDismissByOnBackPressed = true
        isDismissByTouchOutSide = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bottomSheetPickerAdapter != null) {
            bottomSheetPickerAdapter = null
        }
    }

    override fun onInitView() {
        with(binding) {
            binding.tvBottomSheetPickerTitle.text = title
            bottomSheetPickerAdapter!!.listener = object : BottomSheetPickerAdapter.IListener {
                override fun onItemClick(bottomSheetPicker: BottomSheetPicker) {
                    binding.tvBottomSheetPickerSave.setOnSafeClick {
                        chooseItemListener?.invoke(bottomSheetPicker)
                        listener?.onSave(bottomSheetPicker)
                        dismiss()
                    }
                }
            }

            bottomSheetPickerAdapter?.viewType = viewTypeDialog
            rvBottomSheetPicker.apply {
                adapter = bottomSheetPickerAdapter
                layoutManager = MaxItemLayoutManager(context, this@BottomSheetPickerDialog.visibleItem)
            }
            bottomSheetPickerAdapter?.setDataList(lstPickers)
        }
    }

    class Builder {
        var lstPickers: List<BottomSheetPicker> = listOf()
            private set
        var chooseItemListener: ((BottomSheetPicker) -> Unit)? = null
            private set
        var title: String? = null
            private set
        var hint: String? = null
            private set
        var ratioDialogHeight: Float = DEFAULT_RATIO_DIALOG_HEIGHT
            private set
        var visibleItem: Int = MAX_LIST_ITEM
            private set
        var viewTypeDialog: TYPE_PICKER = TYPE_PICKER.DEFAULT
            private set
        var listener: IListener? = null
            private set

        fun setListPicker(lstPickers: List<BottomSheetPicker>) = apply {
            this.lstPickers = lstPickers
        }

        fun setChooseItemListener(listener: (BottomSheetPicker) -> Unit) = apply {
            this.chooseItemListener = listener
        }

        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setHint(hint: String?) = apply {
            this.hint = hint
        }

        fun setRatioDialogHeight(ratio: Float) = apply {
            this.ratioDialogHeight = ratio
        }

        fun setVisibleItem(limit: Int) = apply {
            this.visibleItem = limit
        }

        fun setType(viewTypeDialog: TYPE_PICKER) = apply {
            this.viewTypeDialog = viewTypeDialog
        }

        fun setListener(listener: IListener) = apply {
            this.listener = listener
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            val dialog = BottomSheetPickerDialog(
                lstPickers,
                chooseItemListener,
                title,
                hint,
                ratioDialogHeight,
                visibleItem,
                viewTypeDialog,
                listener
            )
            dialog.show(fragmentManager, null)
        }
    }

    interface IListener {
        fun onSave(bottomSheetPicker: BottomSheetPicker)
    }
}
