package vn.tutorme.mobile.presenter.home.clazz

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.LessonHomeItemBinding
import vn.tutorme.mobile.domain.model.clazz.ClassInfo

class ClassStudentAdapter : TutorMeAdapter() {

    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.lesson_home_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return ClassStudentVH(binding as LessonHomeItemBinding)
    }

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.class_empty_student_item)

    inner class ClassStudentVH(private val binding: LessonHomeItemBinding) : BaseVH<ClassInfo>(binding) {

        init {
            with(binding) {
                tvLessonHomeType.gone()
                tvLessonHomeLesson.gone()
                tvLessonHomePencil.gone()
                tvLessonHomeTimeSlot.gone()
                tvLessonHomeConfirm.gone()
            }

            binding.root.setOnSafeClick {
                getItem {
                    it.classId?.let { it1 -> listener?.onClickInfo(it1) }
                }
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
            }
        }
    }
}
