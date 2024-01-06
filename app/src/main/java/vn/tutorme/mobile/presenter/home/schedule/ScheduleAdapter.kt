package vn.tutorme.mobile.presenter.home.schedule

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.LessonHomeItemBinding
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

class ScheduleAdapter : TutorMeAdapter() {

    var listener: IScheduleListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.lesson_home_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return ScheduleVH(binding as LessonHomeItemBinding)
    }

    override fun getLayoutEmpty(): Empty =
        Empty(layoutResource = R.layout.lesson_empty_teacher_item)

    inner class ScheduleVH(private val binding: LessonHomeItemBinding) : BaseVH<LessonInfo>(binding) {

        init {
            with(binding) {
                tvLessonHomeType.gone()
                ivLessonHomeIcon.gone()
                tvLessonHomeConfirm.gone()
                tvLessonHomePencil.gone()
            }

            binding.root.setOnSafeClick {
                getItem {
                    listener?.onItemClick(it)
                }
            }
        }

        override fun onBind(data: LessonInfo) {
            super.onBind(data)
            with(binding) {
                tvLessonHomeTimeSlot.text = data.getTimeLearnHour()
                tvLessonHomeAvatar.setImageDrawable(
                    when (data.status) {
                        LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.ic_upcoming)
                        LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.ic_happenning)
                        LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.ic_cancel)
                        LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.ic_took_place)
                        else -> getAppDrawable(R.drawable.ic_upcoming)
                    }
                )
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
            }
        }
    }

    interface IScheduleListener {
        fun onItemClick(item: LessonInfo)
    }
}
