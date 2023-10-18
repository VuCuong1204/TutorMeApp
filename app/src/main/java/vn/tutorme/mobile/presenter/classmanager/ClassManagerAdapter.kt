package vn.tutorme.mobile.presenter.classmanager

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.databinding.LessonHomeItemBinding
import vn.tutorme.mobile.domain.model.clazz.ClassInfo

class ClassManagerAdapter : TutorMeAdapter() {

    override fun getLayoutResource(viewType: Int): Int = R.layout.lesson_home_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return ClassManagerVH(binding as LessonHomeItemBinding)
    }

    override fun getLayoutLoading(): Int = R.layout.class_manager_loading_item

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.class_all_empy)

    override fun getLayoutLoadMore(): LoadMore = LoadMore(layoutResource = R.layout.load_more_item)

    inner class ClassManagerVH(private val binding: LessonHomeItemBinding) : BaseVH<ClassInfo>(binding) {

        init {
            with(binding) {
                tvLessonHomeType.gone()
                tvLessonHomeLesson.gone()
                tvLessonHomePencil.gone()
                tvLessonHomeTimeSlot.gone()
                tvLessonHomeConfirm.gone()

                val layoutParams = clLessonHomeRoot.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                clLessonHomeRoot.layoutParams = layoutParams
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
                            getAppDimension(R.dimen.fbase_dimen_4).toInt(),
                            0,
                            getAppDimension(R.dimen.fbase_dimen_96).toInt()
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
                            getAppDimension(R.dimen.fbase_dimen_4).toInt(),
                            0,
                            0
                        )
                    }
                }
                binding.clLessonHomeRoot.layoutParams = params
            }
        }
    }
}
