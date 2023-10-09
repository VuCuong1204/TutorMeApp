package vn.tutorme.mobile.presenter.registerclass

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.databinding.LessonHomeItemBinding
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS
import vn.tutorme.mobile.domain.model.clazz.ClassInfo

class ClassWaitingConfirmAdapter : TutorMeAdapter() {

    var type = CLASS_STATUS.EMPTY_CLASS_STATUS

    override fun getLayoutResource(viewType: Int): Int = R.layout.lesson_home_item

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.class_register_empty)

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return ClassWaitingConfirmVH(binding as LessonHomeItemBinding)
    }

    inner class ClassWaitingConfirmVH(private val binding: LessonHomeItemBinding) : BaseVH<ClassInfo>(binding) {

        init {
            with(binding) {
                tvLessonHomeType.gone()
                tvLessonHomeTimeSlot.gone()
                tvLessonHomePencil.gone()
            }
        }

        override fun onBind(data: ClassInfo) {
            super.onBind(data)

            with(binding) {
                tvLessonHomeAvatar.setImageDrawable(getAppDrawable(R.drawable.ic_upcoming))
                tvLessonHomeState.background = (getAppDrawable(R.drawable.shape_bg_secondary_corner_8))
                tvLessonHomeId.text = data.classId
                tvLessonHomeState.text = data.getTimeDayBegin()
                tvLessonHomeClass.text = data.titleClass
                tvLessonHomeAdvanced.text = data.level
                tvLessonHomeNumber.text = data.getNumberMember()

                val params = binding.clLessonHomeRoot.layoutParams as ViewGroup.MarginLayoutParams
                when (absoluteAdapterPosition) {
                    dataList.lastIndex -> {
                        params.setMargins(
                            0,
                            getAppDimension(R.dimen.fbase_dimen_10).toInt(),
                            0,
                            getAppDimension(R.dimen.fbase_dimen_10).toInt()
                        )
                    }

                    0 -> {
                        params.setMargins(
                            0,
                            getAppDimension(R.dimen.fbase_dimen_20).toInt(),
                            0,
                            0
                        )
                    }

                    else -> {
                        params.setMargins(
                            0,
                            getAppDimension(R.dimen.fbase_dimen_10).toInt(),
                            0,
                            0
                        )
                    }
                }

                if (type == CLASS_STATUS.RECEIVED_STATUS) {
                    tvLessonHomeConfirm.text = getAppString(R.string.reject)
                    tvLessonHomeConfirm.background = getAppDrawable(R.drawable.ripple_bg_comp_corner_12)
                } else {
                    tvLessonHomeConfirm.text = getAppString(R.string.accept_class)
                    tvLessonHomeConfirm.background = getAppDrawable(R.drawable.ripple_bg_primary_corner_14)
                }

                clLessonHomeRoot.layoutParams = params
            }
        }
    }
}
