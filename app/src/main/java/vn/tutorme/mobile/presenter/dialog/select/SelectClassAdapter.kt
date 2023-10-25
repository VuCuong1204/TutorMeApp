package vn.tutorme.mobile.presenter.dialog.select

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.SelectClassItemBinding
import vn.tutorme.mobile.presenter.dialog.select.model.ClassDetailDisplay

class SelectClassAdapter : TutorMeAdapter() {

    var listener: ISelectListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.select_class_item

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return SelectCollectionDiffCallback(oldList, newList)
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectCollectionVH(binding as SelectClassItemBinding)
    }

    inner class SelectCollectionVH(private val binding: SelectClassItemBinding) : BaseVH<ClassDetailDisplay>(binding) {

        init {
            binding.root.setOnSafeClick { getItem { listener?.onItemClick(it) } }
        }

        override fun onBind(data: ClassDetailDisplay) {
            super.onBind(data)
            binding.ivSelectClassTick.isChecked = data.isSelected ?: false
            binding.tvSelectClassName.text = "${data.classInfo?.level} - ${data.classInfo?.getCountLesson()} ${data.classInfo?.getDayBegin()}"
        }

        override fun onBind(data: ClassDetailDisplay, payloads: List<Any>) {
            super.onBind(data, payloads)

            (payloads.firstOrNull() as? List<*>)?.forEach {
                if (it == TICK_COLLECTION_PAYLOAD) {
                    binding.ivSelectClassTick.isChecked = data.isSelected ?: false
                }
            }
        }
    }
}
