package vn.tutorme.mobile.presenter.home.lesson

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.databinding.LessonHomeItemBinding
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

class LessonUnderratedAdapter : TutorMeAdapter() {
    override fun getLayoutResource(viewType: Int): Int = R.layout.lesson_home_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return LessonUnderratedVH(binding as LessonHomeItemBinding)
    }

    inner class LessonUnderratedVH(private val binding: LessonHomeItemBinding) : BaseVH<LessonInfo>(binding) {

        init {
            with(binding) {
                tvLessonHomeType.gone()
                tvLessonHomeConfirm.gone()
            }
        }

        override fun onBind(data: LessonInfo) {
            super.onBind(data)
            with(binding) {
                tvLessonHomeTimeSlot.text = data.getTimeLearnHour()
                tvLessonHomeAvatar.setImageDrawable(getAppDrawable(R.drawable.ic_took_place))
                tvLessonHomeAvatar.background = (getAppDrawable(R.drawable.shape_bg_state_sucess_corner_8))
                tvLessonHomeId.text = data.classId
                tvLessonHomeState.text = data.getTimeBegin()
                tvLessonHomeClass.text = data.getClassTitle()
                tvLessonHomeAdvanced.text = data.level
                tvLessonHomeNumber.text = data.getNumberMemberRatio()
                tvLessonHomeLesson.text = data.getSession()
                tvLessonHomePencil.text = data.getNumberEvaluate()
            }
        }
    }
}
