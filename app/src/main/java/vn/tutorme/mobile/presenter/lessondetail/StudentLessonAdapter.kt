package vn.tutorme.mobile.presenter.lessondetail

import android.text.style.ForegroundColorSpan
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.base.extension.setActionRoleState
import vn.tutorme.mobile.base.extension.setImageTextView
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.databinding.HappenedLessonItemBinding
import vn.tutorme.mobile.databinding.StudentLessonItemBinding
import vn.tutorme.mobile.databinding.TitleLessonDetailBinding
import vn.tutorme.mobile.databinding.TookPlaceLessonItemBinding
import vn.tutorme.mobile.domain.model.authen.ATTENDANCE_STATE
import vn.tutorme.mobile.domain.model.authen.EVALUATE_STATE
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.presenter.lessondetail.model.LESSON_DETAIL_TYPE
import vn.tutorme.mobile.presenter.lessondetail.model.LessonTypeDisplay

class StudentLessonAdapter : TutorMeAdapter() {

    companion object {
        const val UPCOMING_VIEW_TYPE = 10
        const val TOOK_PLACE_VIEW_TYPE = 11
        const val TITLE_VIEW_TYPE = 12
        const val STUDENT_LESSON_VIEW_TYPE = 13
    }

    var listener: IStudentLessonListener? = null

    var lessonType: LESSON_STATUS? = LESSON_STATUS.UPCOMING_STATUS

    override fun getItemViewType(position: Int): Int {
        return when (val item = getDataListAtPosition(position)) {
            is LessonTypeDisplay -> {
                if (item.type == LESSON_STATUS.UPCOMING_STATUS) {
                    UPCOMING_VIEW_TYPE
                } else {
                    TOOK_PLACE_VIEW_TYPE
                }
            }

            is LESSON_DETAIL_TYPE -> TITLE_VIEW_TYPE
            is UserInfo -> STUDENT_LESSON_VIEW_TYPE
            else -> super.getItemViewType(position)
        }
    }

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            UPCOMING_VIEW_TYPE -> R.layout.happened_lesson_item
            TOOK_PLACE_VIEW_TYPE -> R.layout.took_place_lesson_item
            TITLE_VIEW_TYPE -> R.layout.title_lesson_detail
            STUDENT_LESSON_VIEW_TYPE -> R.layout.student_lesson_item
            else -> LAYOUT_INVALID
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            UPCOMING_VIEW_TYPE -> UpComingVH(binding as HappenedLessonItemBinding)
            TOOK_PLACE_VIEW_TYPE -> TookPlaceVH(binding as TookPlaceLessonItemBinding)
            TITLE_VIEW_TYPE -> TitleVH(binding as TitleLessonDetailBinding)
            STUDENT_LESSON_VIEW_TYPE -> StudentLessonVH(binding as StudentLessonItemBinding)
            else -> null
        }
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return StudentLessonDiffCallback(oldList, newList)
    }

    inner class UpComingVH(private val binding: HappenedLessonItemBinding) : BaseVH<LessonTypeDisplay>(binding) {
        override fun onBind(data: LessonTypeDisplay) {
            super.onBind(data)
            with(binding) {
                tvHappenedLessonTime.text = data.lessonInfo?.getTimeLearnHour()
                setActionRoleState(
                    {
                        tvHappenedLessonAttendance.gone()
                        tvHappenedLessonJoinRoom.gone()
                    },
                    {
                        tvHappenedLessonAttendance.show()
                        if (data.userInfo?.evaluateState == EVALUATE_STATE.NO_EVALUATE_STATE) {
                            tvHappenedLessonAttendance.apply {
                                text = getAppString(R.string.attendance)
                                setTextColor(getAppColor(R.color.primary))
                                background = getAppDrawable(R.drawable.ripple_bg_white_corner_14_stroke_1)
                                setImageTextView(getAppDrawable(R.drawable.ic_badge_inactive))
                                isEnabled = true
                            }
                        } else {
                            tvHappenedLessonAttendance.apply {
                                text = getAppString(R.string.took_attendance)
                                setTextColor(getAppColor(R.color.white))
                                background = getAppDrawable(R.drawable.ripple_bg_primary_corner_14)
                                setImageTextView(getAppDrawable(R.drawable.ic_badge_active))
                                isEnabled = false
                            }
                        }
                        if (data.lessonInfo?.status == LESSON_STATUS.HAPPENING_STATUS) {
                            tvHappenedLessonJoinRoom.show()
                        } else {
                            tvHappenedLessonJoinRoom.gone()
                        }
                    }
                )
            }
        }

        override fun onBind(data: LessonTypeDisplay, payloads: List<Any>) {
            super.onBind(data, payloads)
            with(binding) {
                (payloads.firstOrNull() as? List<*>)?.forEach {
                    when (it) {
                        ACTION_ATTENDANCE_PAYLOAD -> {
                            setActionRoleState(
                                {
                                    tvHappenedLessonAttendance.gone()
                                    tvHappenedLessonJoinRoom.gone()
                                },
                                {
                                    tvHappenedLessonAttendance.show()
                                    if (data.userInfo?.evaluateState == EVALUATE_STATE.NO_EVALUATE_STATE) {
                                        tvHappenedLessonAttendance.apply {
                                            text = getAppString(R.string.attendance)
                                            setTextColor(getAppColor(R.color.primary))
                                            background = getAppDrawable(R.drawable.ripple_bg_white_corner_14_stroke_1)
                                            setImageTextView(getAppDrawable(R.drawable.ic_badge_inactive))
                                            isEnabled = true
                                        }
                                    } else {
                                        tvHappenedLessonAttendance.apply {
                                            text = getAppString(R.string.took_attendance)
                                            setTextColor(getAppColor(R.color.white))
                                            background = getAppDrawable(R.drawable.ripple_bg_primary_corner_14)
                                            setImageTextView(getAppDrawable(R.drawable.ic_badge_active))
                                            isEnabled = false
                                        }
                                    }
                                    if (data.lessonInfo?.status == LESSON_STATUS.HAPPENING_STATUS) {
                                        tvHappenedLessonJoinRoom.show()
                                    } else {
                                        tvHappenedLessonJoinRoom.gone()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    inner class TookPlaceVH(private val binding: TookPlaceLessonItemBinding) : BaseVH<LessonTypeDisplay>(binding) {
        override fun onBind(data: LessonTypeDisplay) {
            super.onBind(data)
            with(binding) {
                val text = SpannableBuilder()
                    .appendText(getAppString(R.string.have_evaluated))
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
                    .appendText(" ${data.lessonInfo?.getNumberMemberRatio()} ")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.status_warring)))
                    .appendText(getAppString(R.string.student))
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
                    .spannedText

                tvTookPlaceLessonRate.text = text

                if (data.userInfo?.attendanceState == ATTENDANCE_STATE.ROLL_CALLED_STATE) {
                    tvTookPlaceLessonAttendanceState.text = getAppString(R.string.took_attendance)
                    tvTookPlaceLessonAttendanceState.setImageTextView(getAppDrawable(R.drawable.ic_tick_primary))
                    tvTookPlaceLessonAttendanceState.setTextColor(getAppColor(R.color.primary))
                    tvTookPlaceLessonType.text = getAppString(R.string.obtain)
                    tvTookPlaceLessonType.background = getAppDrawable(R.drawable.shape_bg_green_corner_8_stroke_green)
                    tvTookPlaceLessonType.setImageTextView(getAppDrawable(R.drawable.ic_prize_success))
                    tvTookPlaceLessonType.setTextColor(getAppColor(R.color.status_success))
                } else {
                    tvTookPlaceLessonAttendanceState.text = getAppString(R.string.took_attendance_yet)
                    tvTookPlaceLessonAttendanceState.setImageTextView(getAppDrawable(R.drawable.ic_delete_orange))
                    tvTookPlaceLessonAttendanceState.setTextColor(getAppColor(R.color.status_warring))
                    tvTookPlaceLessonType.text = getAppString(R.string.obtain_no)
                    tvTookPlaceLessonType.background = getAppDrawable(R.drawable.shape_bg_red_corner_8_stroke_red)
                    tvTookPlaceLessonType.setImageTextView(getAppDrawable(R.drawable.ic_prize_error))
                    tvTookPlaceLessonType.setTextColor(getAppColor(R.color.red_30))
                }

                tvTookPlaceLessonStartTime.text = data.lessonInfo?.getHourBegin()
                tvTookPlaceLessonEndTime.text = data.lessonInfo?.getHourEnd()

                if (data.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
                    tvTookPlaceLessonAttendanceState.gone()
                    tvTookPlaceLessonType.gone()
                    tvTookPlaceLessonRate.show()
                } else {
                    tvTookPlaceLessonAttendanceState.show()
                    tvTookPlaceLessonType.show()
                    tvTookPlaceLessonRate.gone()
                }
            }
        }
    }

    inner class TitleVH(private val binding: TitleLessonDetailBinding) : BaseVH<LESSON_DETAIL_TYPE>(binding) {
        override fun onBind(data: LESSON_DETAIL_TYPE) {
            super.onBind(data)

            if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
                binding.tvTitleLessonPlease.show()
            } else {
                binding.tvTitleLessonPlease.gone()
            }
        }
    }

    inner class StudentLessonVH(private val binding: StudentLessonItemBinding) : BaseVH<UserInfo>(binding) {

        init {
            binding.tvStudentLessonRate.setOnSafeClick {
                getItem {
                    listener?.onRateClick(it)
                }
            }
        }

        override fun onBind(data: UserInfo) {
            super.onBind(data)
            with(binding) {

                ivStudentLessonAvatar.loadUser(
                    url = data.avatar,
                    placeHolder = if (data.gender == GENDER_TYPE.MALE_TYPE) getAppDrawable(R.drawable.bg_gender_male)
                    else getAppDrawable(R.drawable.bg_gender_female)
                )

                tvStudentLessonName.text = data.fullName
                tvStudentLessonGender.text = data.getGenderUser()
                tvStudentLessonDate.text = data.date

                when (lessonType) {
                    LESSON_STATUS.UPCOMING_STATUS -> {
                        tvStudentLessonAttendance.gone()
                        tvStudentLessonRate.gone()
                    }

                    LESSON_STATUS.HAPPENING_STATUS -> {
                        tvStudentLessonAttendance.show()
                        tvStudentLessonRate.gone()
                        if (data.attendanceState == ATTENDANCE_STATE.ROLL_CALLED_STATE) {
                            tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_green_corner_8_stroke_green)
                            tvStudentLessonAttendance.text = getAppString(R.string.took_attendance)
                            tvStudentLessonAttendance.setTextColor(getAppColor(R.color.status_success))
                        } else {
                            tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_red_corner_8_stroke_red)
                            tvStudentLessonAttendance.text = getAppString(R.string.took_attendance_yet)
                            tvStudentLessonAttendance.setTextColor(getAppColor(R.color.red_30))
                        }
                    }

                    LESSON_STATUS.TOOK_PLACE_STATUS -> {
                        tvStudentLessonAttendance.show()
                        tvStudentLessonRate.show()
                        if (data.attendanceState == ATTENDANCE_STATE.ROLL_CALLED_STATE) {
                            tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_green_corner_8_stroke_green)
                            tvStudentLessonAttendance.text = getAppString(R.string.took_attendance)
                            tvStudentLessonAttendance.setTextColor(getAppColor(R.color.status_success))
                        } else {
                            tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_red_corner_8_stroke_red)
                            tvStudentLessonAttendance.text = getAppString(R.string.took_attendance_yet)
                            tvStudentLessonAttendance.setTextColor(getAppColor(R.color.red_30))
                            tvStudentLessonRate.gone()
                        }

                        if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
                            if (AppPreferences.userInfo?.userId == data.userId) {
                                tvStudentLessonRate.show()
                            } else {
                                tvStudentLessonRate.gone()
                            }
                            tvStudentLessonRate.background = getAppDrawable(R.drawable.ripple_bg_purple_corner_14)
                            tvStudentLessonRate.text = getAppString(R.string.view_evaluate)
                            tvStudentLessonRate.setTextColor(getAppColor(R.color.primary))
                        } else {
                            if (data.evaluateState == EVALUATE_STATE.HAVE_EVALUATE_STATE) {
                                tvStudentLessonRate.background = getAppDrawable(R.drawable.ripple_bg_purple_corner_14)
                                tvStudentLessonRate.text = getAppString(R.string.view_evaluate)
                                tvStudentLessonRate.setTextColor(getAppColor(R.color.primary))
                            } else {
                                tvStudentLessonRate.background = getAppDrawable(R.drawable.ripple_bg_red_60_corner_14)
                                tvStudentLessonRate.text = getAppString(R.string.evaluate)
                                tvStudentLessonRate.setTextColor(getAppColor(R.color.red_50))
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        override fun onBind(data: UserInfo, payloads: List<Any>) {
            super.onBind(data, payloads)
            with(binding) {
                (payloads.firstOrNull() as? List<*>)?.forEach {
                    when (it) {
                        ATTENDANCE_PAYLOAD -> {
                            when (lessonType) {
                                LESSON_STATUS.UPCOMING_STATUS -> {
                                    tvStudentLessonAttendance.gone()
                                    tvStudentLessonRate.gone()
                                }

                                LESSON_STATUS.HAPPENING_STATUS -> {
                                    tvStudentLessonAttendance.show()
                                    tvStudentLessonRate.gone()
                                    if (data.attendanceState == ATTENDANCE_STATE.ROLL_CALLED_STATE) {
                                        tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_green_corner_8_stroke_green)
                                        tvStudentLessonAttendance.text = getAppString(R.string.took_attendance)
                                        tvStudentLessonAttendance.setTextColor(getAppColor(R.color.status_success))
                                    } else {
                                        tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_red_corner_8_stroke_red)
                                        tvStudentLessonAttendance.text = getAppString(R.string.took_attendance_yet)
                                        tvStudentLessonAttendance.setTextColor(getAppColor(R.color.red_30))
                                    }
                                }

                                LESSON_STATUS.TOOK_PLACE_STATUS -> {
                                    tvStudentLessonAttendance.show()
                                    tvStudentLessonRate.show()
                                    if (data.attendanceState == ATTENDANCE_STATE.ROLL_CALLED_STATE) {
                                        tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_green_corner_8_stroke_green)
                                        tvStudentLessonAttendance.text = getAppString(R.string.took_attendance)
                                        tvStudentLessonAttendance.setTextColor(getAppColor(R.color.status_success))
                                    } else {
                                        tvStudentLessonAttendance.background = getAppDrawable(R.drawable.shape_bg_red_corner_8_stroke_red)
                                        tvStudentLessonAttendance.text = getAppString(R.string.took_attendance_yet)
                                        tvStudentLessonAttendance.setTextColor(getAppColor(R.color.red_30))
                                        tvStudentLessonRate.gone()
                                    }

                                    if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
                                        if (AppPreferences.userInfo?.userId == data.userId) {
                                            tvStudentLessonRate.show()
                                        } else {
                                            tvStudentLessonRate.gone()
                                        }
                                        tvStudentLessonRate.background = getAppDrawable(R.drawable.ripple_bg_purple_corner_14)
                                        tvStudentLessonRate.text = getAppString(R.string.view_evaluate)
                                        tvStudentLessonRate.setTextColor(getAppColor(R.color.primary))
                                    } else {
                                        if (data.evaluateState == EVALUATE_STATE.HAVE_EVALUATE_STATE) {
                                            tvStudentLessonRate.background = getAppDrawable(R.drawable.ripple_bg_purple_corner_14)
                                            tvStudentLessonRate.text = getAppString(R.string.view_evaluate)
                                            tvStudentLessonRate.setTextColor(getAppColor(R.color.primary))
                                        } else {
                                            tvStudentLessonRate.background = getAppDrawable(R.drawable.ripple_bg_red_60_corner_14)
                                            tvStudentLessonRate.text = getAppString(R.string.evaluate)
                                            tvStudentLessonRate.setTextColor(getAppColor(R.color.red_50))
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
