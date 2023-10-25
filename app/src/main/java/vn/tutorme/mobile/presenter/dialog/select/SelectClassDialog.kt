package vn.tutorme.mobile.presenter.dialog.select

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.SelectClassDialogBinding
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.presenter.dialog.select.model.ClassDetailDisplay

class SelectClassDialog : TutorMeDialog<SelectClassDialogBinding>(R.layout.select_class_dialog) {

    private val collectionAdapter by lazy { SelectClassAdapter() }
    var listener: IListener? = null
    var classInfo: ClassInfo? = null
    var classDetailDisplayList: List<ClassDetailDisplay> = listOf()

    override fun getBackgroundId() = R.id.flSelectClass

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.NORMAL
        isFullWidth = false
        isFullHeight = false
        isDismissByTouchOutSide = true
        isDismissByOnBackPressed = true
    }

    override fun onInitView() {

        binding.ivSelectClassClose.setOnSafeClick {
            dismiss()
        }

        binding.tvSelectClassCancel.setOnSafeClick {
            dismiss()
        }

        binding.tvSelectClassConfirm.setOnSafeClick {
            classInfo?.let { value -> listener?.onConfirmClick(value) }
        }

        setAdapter()
    }

    private fun setAdapter() {
        binding.cvSelectClassSelect.apply {
            setBaseAdapter(collectionAdapter)
            submitList(classDetailDisplayList)
        }

        collectionAdapter.apply {
            listener = object : ISelectListener {
                override fun onItemClick(item: ClassDetailDisplay) {
                    val newList = classDetailDisplayList.toMutableList()
                    val oldItem = newList.find { it.isSelected == true }
                    if (oldItem != null) {
                        val oldIndex = newList.indexOf(oldItem)
                        newList[oldIndex] = oldItem.copy(isSelected = false)
                    }

                    val newIndex = newList.indexOf(item)
                    if (newIndex in 0..newList.lastIndex) {
                        newList[newIndex] = item.copy(isSelected = true)
                        binding.cvSelectClassSelect.submitList(newList)
                        classInfo = item.classInfo
                    }
                }
            }
        }
    }

    interface IListener {
        fun onConfirmClick(item: ClassInfo)
    }
}
