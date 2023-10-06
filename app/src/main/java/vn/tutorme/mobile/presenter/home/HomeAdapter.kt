package vn.tutorme.mobile.presenter.home

import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.hide
import vn.tutorme.mobile.base.extension.loadImage
import vn.tutorme.mobile.base.extension.setImageTextView
import vn.tutorme.mobile.base.extension.setOnTouchClick
import vn.tutorme.mobile.databinding.BannerHomeItemBinding
import vn.tutorme.mobile.databinding.CourseItemBinding
import vn.tutorme.mobile.databinding.LessonItemBinding
import vn.tutorme.mobile.databinding.MissionHomeItemBinding
import vn.tutorme.mobile.databinding.TitleHomeItemBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.banner.Banner
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.mission.MissionInfo
import vn.tutorme.mobile.presenter.home.banner.BannerHomeAdapter
import vn.tutorme.mobile.presenter.home.clazz.ClassStudentAdapter
import vn.tutorme.mobile.presenter.home.clazz.ClassTeacherAdapter
import vn.tutorme.mobile.presenter.home.lesson.LessonStudentAdapter
import vn.tutorme.mobile.presenter.home.lesson.LessonUnderratedAdapter
import vn.tutorme.mobile.presenter.home.schedule.ScheduleAdapter

class HomeAdapter : TutorMeAdapter() {

    companion object {
        const val BANNER_TYPE = 10
        const val MISSION_TYPE = 11
        const val SCHEDULE_TYPE = 12
        const val LESSON_TYPE = 13
        const val CLASS_TEACHER_TYPE = 14
        const val COURSE_TYPE = 15
        const val TITLE_TYPE = 16
        const val CLASS_STUDENT_TYPE = 17
        const val LESSON_STUDENT_TYPE = 18
        const val FIRST_POSITION = 0
    }

    override fun getItemViewType(position: Int): Int {
        val item = getDataListAtPosition(position)
        return if (item is List<*>) {
            when (item[FIRST_POSITION]) {
                is Banner -> BANNER_TYPE
                is LessonInfo -> {
                    if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
                        LESSON_STUDENT_TYPE
                    } else {
                        if ((item[FIRST_POSITION] as LessonInfo).getAssessmentState()) {
                            LESSON_TYPE
                        } else {
                            SCHEDULE_TYPE
                        }
                    }
                }

                is ClassInfo -> {
                    if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
                        CLASS_TEACHER_TYPE
                    } else {
                        CLASS_STUDENT_TYPE
                    }
                }

                else -> super.getItemViewType(position)
            }
        } else {
            when (item) {
                is MissionInfo -> MISSION_TYPE
                is CourseInfo -> COURSE_TYPE
                is TITLE_HOME_TYPE -> TITLE_TYPE
                else -> super.getItemViewType(position)
            }
        }
    }

    override fun getLayoutLoading(): Int {
        return if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
            R.layout.home_student_loading_item
        } else {
            R.layout.home_teacher_loading_item
        }
    }

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            BANNER_TYPE -> R.layout.banner_home_item
            SCHEDULE_TYPE, LESSON_TYPE, LESSON_STUDENT_TYPE, CLASS_TEACHER_TYPE, CLASS_STUDENT_TYPE -> R.layout.lesson_item
            MISSION_TYPE -> R.layout.mission_home_item
            COURSE_TYPE -> R.layout.course_item
            TITLE_TYPE -> R.layout.title_home_item
            else -> LAYOUT_INVALID
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            BANNER_TYPE -> BannerVH(binding as BannerHomeItemBinding)
            SCHEDULE_TYPE -> ScheduleVH(binding as LessonItemBinding)
            LESSON_TYPE -> LessonUnderratedVH(binding as LessonItemBinding)
            LESSON_STUDENT_TYPE -> LessonStudentVH(binding as LessonItemBinding)
            CLASS_TEACHER_TYPE -> ClassTeacherVH(binding as LessonItemBinding)
            CLASS_STUDENT_TYPE -> ClassStudentVH(binding as LessonItemBinding)
            MISSION_TYPE -> MissionVH(binding as MissionHomeItemBinding)
            TITLE_TYPE -> TitleVH(binding as TitleHomeItemBinding)
            COURSE_TYPE -> CourseVH(binding as CourseItemBinding)
            else -> null
        }
    }

    inner class BannerVH(private val binding: BannerHomeItemBinding) : BaseVH<List<Banner>>(binding) {

        private val bannerAdapter by lazy { BannerHomeAdapter() }

        init {
            binding.vpBannerHomeRoot.apply {
                adapter = bannerAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }

            binding.crBannerHomeNext.setViewPager2(binding.vpBannerHomeRoot)
        }

        override fun onBind(data: List<Banner>) {
            super.onBind(data)

            bannerAdapter.setDataList(data)
        }
    }

    inner class TitleVH(private val binding: TitleHomeItemBinding) : BaseVH<TITLE_HOME_TYPE>(binding) {

        init {
            binding.tvTitleHomeViewMore.setOnTouchClick(colorResUp = R.color.primary) {}
        }

        override fun onBind(data: TITLE_HOME_TYPE) {
            super.onBind(data)

            binding.tvTitleHomeRoot.text = data.value

            binding.tvTitleHomeViewMore.text = if (data == TITLE_HOME_TYPE.MISSION_TYPE) {
                getAppString(R.string.view_detail)
            } else {
                getAppString(R.string.view_all)
            }
        }
    }

    inner class MissionVH(private val binding: MissionHomeItemBinding) : BaseVH<MissionInfo>(binding) {
        override fun onBind(data: MissionInfo) {
            super.onBind(data)
            with(binding) {
                tvMissionHomeWeek.text = data.getTimeWeek()

                if (data.getMissionCasesState()) {
                    ivMissionHomeTick.setImageDrawable(getAppDrawable(R.drawable.ic_tick_show))
                } else {
                    ivMissionHomeTick.setImageDrawable(getAppDrawable(R.drawable.ic_tick_disable))
                }

                if (data.getMissionSessionsEvaluateState()) {
                    ivMissionHomeTickLesson.setImageDrawable(getAppDrawable(R.drawable.ic_tick_show))
                } else {
                    ivMissionHomeTickLesson.setImageDrawable(getAppDrawable(R.drawable.ic_tick_disable))
                }

                if (data.getMissionCasesState() && data.getMissionSessionsEvaluateState()) {
                    tvMissionHomeNotify.text = getAppString(R.string.mission_home_complete)
                    tvMissionHomeNotify.setTextColor(getAppColor(R.color.status_success))
                    tvMissionHomeNotify.setImageTextView(left = getAppDrawable(R.drawable.ic_mission_cup_complete))
                    ivMissionHomeState.setImageResource(R.drawable.ic_mission_complete)
                } else if (data.getMissionEmptyState()) {
                    tvMissionHomeNotify.text = getAppString(R.string.mission_home_empty)
                    tvMissionHomeNotify.setTextColor(getAppColor(R.color.text1))
                    tvMissionHomeNotify.setImageTextView(left = getAppDrawable(R.drawable.ic_mission_loudspeaker_unfinished))
                    ivMissionHomeState.setImageResource(R.drawable.ic_mission_empty)
                    ivMissionHomeTickLesson.hide()
                    ivMissionHomeTick.hide()
                } else {
                    tvMissionHomeNotify.text = getAppString(R.string.mission_home_no_complete)
                    tvMissionHomeNotify.setTextColor(getAppColor(R.color.status_warring))
                    tvMissionHomeNotify.setImageTextView(left = getAppDrawable(R.drawable.ic_mission_loudspeaker_unfinished))
                    ivMissionHomeState.setImageResource(R.drawable.ic_mission_unfinished)
                }

                val textCase = SpannableBuilder()
                    .appendText("${data.numberCasesTaught}/")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
                    .appendText("${data.totalCases}")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.text1)))
                    .spannedText

                binding.tvMissionHomeCase.text = textCase

                val textEvaluate = SpannableBuilder()
                    .appendText("${data.numberSessionsEvaluate}/")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
                    .appendText("${data.totalSessionsEvaluate}")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.text1)))
                    .spannedText

                binding.tvMissionHomeCountLesson.text = textEvaluate
            }
        }
    }

    inner class ScheduleVH(private val binding: LessonItemBinding) : BaseVH<List<LessonInfo>>(binding) {

        private val scheduleAdapter by lazy { ScheduleAdapter() }

        init {
            binding.cvLessonRoot.apply {
                setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL)
                setBaseAdapter(scheduleAdapter)
            }
        }

        override fun onBind(data: List<LessonInfo>) {
            super.onBind(data)
            val listDataNew = data.toMutableList()
            if (listDataNew.first().lessonId == null) {
                listDataNew.removeAt(0)
            }
            binding.cvLessonRoot.submitList(listDataNew)
        }
    }

    inner class LessonUnderratedVH(private val binding: LessonItemBinding) : BaseVH<List<LessonInfo>>(binding) {

        private val lessonAdapter by lazy { LessonUnderratedAdapter() }

        init {
            binding.cvLessonRoot.apply {
                setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL)
                setBaseAdapter(lessonAdapter)
            }
        }

        override fun onBind(data: List<LessonInfo>) {
            super.onBind(data)
            val listDataNew = data.toMutableList()
            if (listDataNew.first().lessonId == null) {
                listDataNew.removeAt(0)
            }
            binding.cvLessonRoot.submitList(listDataNew)
        }
    }

    inner class LessonStudentVH(private val binding: LessonItemBinding) : BaseVH<List<LessonInfo>>(binding) {

        private val lessonAdapter by lazy { LessonStudentAdapter() }

        init {
            binding.cvLessonRoot.apply {
                setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL)
                setBaseAdapter(lessonAdapter)
            }
        }

        override fun onBind(data: List<LessonInfo>) {
            super.onBind(data)
            val listDataNew = data.toMutableList()
            if (listDataNew.first().lessonId == null) {
                listDataNew.removeAt(0)
            }
            binding.cvLessonRoot.submitList(listDataNew)
        }
    }

    inner class ClassTeacherVH(private val binding: LessonItemBinding) : BaseVH<List<ClassInfo>>(binding) {

        private val classAdapter by lazy { ClassTeacherAdapter() }

        init {
            binding.cvLessonRoot.apply {
                setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL)
                setBaseAdapter(classAdapter)
            }
        }

        override fun onBind(data: List<ClassInfo>) {
            super.onBind(data)

            val params = binding.cvLessonRoot.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 0, 0, getAppDimension(R.dimen.fbase_dimen_96).toInt())
            binding.cvLessonRoot.layoutParams = params

            val listDataNew = data.toMutableList()
            if (listDataNew.first().classId == null) {
                listDataNew.removeAt(0)
            }
            binding.cvLessonRoot.submitList(listDataNew)
        }
    }

    inner class ClassStudentVH(private val binding: LessonItemBinding) : BaseVH<List<ClassInfo>>(binding) {

        private val classAdapter by lazy { ClassStudentAdapter() }

        init {
            binding.cvLessonRoot.apply {
                setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_HORIZONTAL)
                setBaseAdapter(classAdapter)
            }
        }

        override fun onBind(data: List<ClassInfo>) {
            super.onBind(data)

            val listDataNew = data.toMutableList()
            if (listDataNew.first().classId == null) {
                listDataNew.removeAt(0)
            }
            binding.cvLessonRoot.submitList(listDataNew)
        }
    }

    inner class CourseVH(private val binding: CourseItemBinding) : BaseVH<CourseInfo>(binding) {

        override fun onBind(data: CourseInfo) {
            super.onBind(data)

            with(binding) {
                ivCourseAvatar.loadImage(data.banner)
                tvCourseTitle.text = data.title

                val text = SpannableBuilder()
                    .appendText("${data.ratingTotal} ")
                    .withSpanImage(getAppDrawable(R.drawable.ic_star))
                    .appendText(" ${String.format(getAppString(R.string.rating_count), data.ratingNumber)}")
                    .spannedText

                tvCourseRate.text = text
                tvCourseTime.text = data.getTimeLesson()
                tvCourseCountMember.text = data.getCountPeopleRegister()
                tvCourseDate.text = data.getTimeExpired()
                tvCoursePrice.text = data.getPriceValue()

                if (absoluteAdapterPosition == dataList.size - 1) {
                    val params = clCourseRoot.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(getAppDimension(R.dimen.fbase_dimen_16).toInt(), getAppDimension(R.dimen.fbase_dimen_14).toInt(), getAppDimension(R.dimen.fbase_dimen_16).toInt(), getAppDimension(R.dimen.fbase_dimen_96).toInt())
                    clCourseRoot.layoutParams = params
                }
            }
        }
    }
}
