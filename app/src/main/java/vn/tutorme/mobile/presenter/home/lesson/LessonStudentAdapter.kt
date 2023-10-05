package vn.tutorme.mobile.presenter.home.lesson

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.databinding.LessonHomeItemBinding
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

class LessonStudentAdapter : TutorMeAdapter() {
    override fun getLayoutResource(viewType: Int): Int = R.layout.lesson_home_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return LessonStudentVH(binding as LessonHomeItemBinding)
    }

    inner class LessonStudentVH(private val binding: LessonHomeItemBinding) : BaseVH<LessonInfo>(binding) {

        init {
            with(binding) {
                tvLessonHomeType.gone()
                tvLessonHomeConfirm.gone()
                tvLessonHomePencil.gone()
                ivLessonHomeIcon.gone()
            }
        }

        override fun onBind(data: LessonInfo) {
            super.onBind(data)
            with(binding) {
                tvLessonHomeTimeSlot.text = data.getTimeLearnHour()
                tvLessonHomeAvatar.setImageDrawable(getAppDrawable(R.drawable.ic_took_place))
                tvLessonHomeState.background = (getAppDrawable(R.drawable.shape_bg_state_sucess_corner_8))
                tvLessonHomeId.text = data.classId
                tvLessonHomeState.text = when (data.status) {
                    LESSON_STATUS.UPCOMING_STATUS -> getAppString(R.string.upcoming)
                    LESSON_STATUS.HAPPENING_STATUS -> getAppString(R.string.happening)
                    LESSON_STATUS.CANCEL_STATUS -> getAppString(R.string.cancel)
                    LESSON_STATUS.TOOK_PLACE_STATUS -> getAppString(R.string.took_place)
                    else -> getAppString(R.string.upcoming)
                }
                tvLessonHomeClass.text = data.getClassTitle()
                tvLessonHomeAdvanced.text = data.level
                tvLessonHomeNumber.text = data.getNumberMember()
                tvLessonHomeLesson.text = data.getSession()
                tvLessonHomePencil.text = data.getNumberEvaluate()
            }
        }
    }
}
