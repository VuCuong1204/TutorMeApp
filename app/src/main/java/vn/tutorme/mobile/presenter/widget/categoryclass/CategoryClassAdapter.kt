package vn.tutorme.mobile.presenter.widget.categoryclass

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDimensionPixel
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setCustomFont
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.databinding.CategoryClassItemBinding
import vn.tutorme.mobile.domain.model.category.Category

class CategoryClassAdapter : TutorMeAdapter() {

    companion object {
        const val ROW_CLASS_VIEW_TYPE = 11
    }

    var listener: IListenerCategory? = null

    var builder: Builder? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.category_class_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return CategoryClassVH(binding as CategoryClassItemBinding)
    }

    override fun getColumnInRow(viewType: Int): Int = dataList.size

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return CategoryClassDiffCallback(oldList, newList)
    }

    inner class CategoryClassVH(val binding: CategoryClassItemBinding) : BaseVH<Category>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem { item ->
                    listener?.onItemClick(absoluteAdapterPosition, item)
                }
            }
        }

        override fun onBind(data: Category) {
            super.onBind(data)

            binding.clCategoryClassRoot.setPadding(
                0,
                getAppDimensionPixel(R.dimen.fbase_corner_12),
                0,
                getAppDimensionPixel(R.dimen.fbase_corner_12
                )
            )

            binding.tvCategoryClassTitle.text = data.name

            val params = binding.clCategoryClassRoot.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(
                builder?.marginStart ?: 0,
                0,
                builder?.marginEnd ?: 0,
                0
            )

            binding.clCategoryClassRoot.layoutParams = params

            if (data.checked == true) {

                if (builder?.fontSelected != null) {
                    binding.tvCategoryClassTitle.setCustomFont(fontPath = builder?.fontSelected!!)
                } else {
                    binding.tvCategoryClassTitle.setCustomFont(fontPath = R.font.font_raleway_bold)
                }

                if (builder?.bgSelected != null) {
                    binding.clCategoryClassRoot.background = builder?.bgSelected
                    binding.vCategoryClass.gone()
                } else {
                    binding.vCategoryClass.show()
                }

                binding.tvCategoryClassTitle.setTextColor(
                    if (builder?.textColorSelected != null) {
                        builder?.textColorSelected!!
                    } else {
                        getAppColor(R.color.primary)
                    }
                )
            } else {
                if (builder?.fontHide != null) {
                    binding.tvCategoryClassTitle.setCustomFont(fontPath = builder?.fontHide!!)
                } else {
                    binding.tvCategoryClassTitle.setCustomFont(fontPath = R.font.font_raleway_medium)
                }

                builder?.textColorHide?.let { color ->
                    binding.tvCategoryClassTitle.setTextColor(color)
                }

                builder?.bgHide?.let {
                    binding.clCategoryClassRoot.background = it
                    binding.vCategoryClass.gone()
                }

                binding.vCategoryClass.gone()
                binding.tvCategoryClassTitle.setTextColor(getAppColor(R.color.text1))
            }
        }

        override fun onBind(data: Category, payloads: List<Any>) {
            super.onBind(data, payloads)
            (payloads.firstOrNull() as? List<*>)?.forEach {
                when (it) {
                    CHECKED_PAYLOAD -> {

                        if (data.checked == true) {

                            if (builder?.fontSelected != null) {
                                binding.tvCategoryClassTitle.setCustomFont(fontPath = builder?.fontSelected!!)
                            } else {
                                binding.tvCategoryClassTitle.setCustomFont(fontPath = R.font.font_raleway_bold)
                            }

                            if (builder?.bgSelected != null) {
                                binding.clCategoryClassRoot.background = builder?.bgSelected
                                binding.vCategoryClass.gone()
                            } else {
                                binding.vCategoryClass.show()
                            }

                            builder?.textColorSelected?.let { color ->
                                binding.tvCategoryClassTitle.setTextColor(color)
                            }


                            binding.tvCategoryClassTitle.setTextColor(
                                if (builder?.textColorSelected != null) {
                                    builder?.textColorSelected!!
                                } else {
                                    getAppColor(R.color.primary)
                                }
                            )
                        } else {
                            if (builder?.fontHide != null) {
                                binding.tvCategoryClassTitle.setCustomFont(fontPath = builder?.fontHide!!)
                            } else {
                                binding.tvCategoryClassTitle.setCustomFont(fontPath = R.font.font_raleway_medium)
                            }

                            builder?.textColorHide?.let { color ->
                                binding.tvCategoryClassTitle.setTextColor(color)
                            }

                            builder?.bgHide?.let {
                                binding.clCategoryClassRoot.background = it
                            }

                            binding.vCategoryClass.gone()
                            binding.tvCategoryClassTitle.setTextColor(getAppColor(R.color.text1))
                        }
                    }
                }
            }
        }
    }

    data class Builder(
        var textColorHide: Int? = null,
        var textColorSelected: Int? = null,
        var fontHide: Int? = null,
        var fontSelected: Int? = null,
        var widthView: Int? = null,
        var bgHide: Drawable? = null,
        var bgSelected: Drawable? = null,
        var marginStart: Int? = null,
        var marginEnd: Int? = null
    )
}
