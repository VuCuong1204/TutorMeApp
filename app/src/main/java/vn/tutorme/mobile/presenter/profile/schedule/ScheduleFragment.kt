package vn.tutorme.mobile.presenter.profile.schedule

import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppDimensionPixel
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setCustomFont
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ScheduleFragmentBinding
import vn.tutorme.mobile.domain.model.profile.DAY_TYPE
import vn.tutorme.mobile.domain.model.profile.schedule.ScheduleInfo
import vn.tutorme.mobile.utils.TimeUtils
import vn.tutorme.mobile.utils.TimeUtils.DATE_FORMAT

@AndroidEntryPoint
class ScheduleFragment : TutorMeFragment<ScheduleFragmentBinding>(R.layout.schedule_fragment) {

    private val viewModel by viewModels<ScheduleViewModel>()
    private val hasMap = hashMapOf<Int, Int>()

    override fun onInitView() {
        super.onInitView()

        val titleStart = TimeUtils.convertTimeToDay(TimeUtils.getStartOfWeek(), DATE_FORMAT)
        val titleEnd = TimeUtils.convertTimeToDay(TimeUtils.getEndOfWeek(), DATE_FORMAT)
        binding.tvScheduleTime.text = String.format(getAppString(R.string.between_time_week), titleStart, titleEnd)
        addIdHasMap()

        binding.ivScheduleBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.lessonState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    it.data?.let { it1 -> addHeader(it1) }
                }
            })
        }
    }

    private fun addHeader(dataList: List<ScheduleInfo>) {
        dataList.forEachIndexed { index, scheduleInfo ->
            val textView = TextView(mainActivity)
            textView.text = "${scheduleInfo.classId} \n ${scheduleInfo.className} \n ${scheduleInfo.content} \n ${scheduleInfo.getLessonName()}"
            textView.gravity = Gravity.CENTER
            textView.setCustomFont(fontPath = R.font.font_worksans_medium)
            textView.textSize = getAppDimensionPixel(R.dimen.fbase_dimen_6).toFloat()
            textView.setTextColor(ContextCompat.getColor(mainActivity, R.color.white))
            textView.setBackgroundColor(ContextCompat.getColor(mainActivity, viewModel.getColorList().random()))
            val layoutParams = ConstraintLayout.LayoutParams(0, 0)

            layoutParams.topToTop = if (hasMap.contains(scheduleInfo.timeBegin)) {
                hasMap[scheduleInfo.timeBegin]!!
            } else {
                addGuide(scheduleInfo.timeBegin ?: INT_DEFAULT)
                hasMap[scheduleInfo.timeBegin]!!
            }

            layoutParams.bottomToBottom = if (hasMap.contains(scheduleInfo.timeEnd)) {
                hasMap[scheduleInfo.timeEnd]!!
            } else {
                addGuide(scheduleInfo.timeEnd ?: INT_DEFAULT)
                hasMap[scheduleInfo.timeEnd]!!
            }

            layoutParams.startToStart = getDayId(scheduleInfo.rank ?: DAY_TYPE.MONDAY)
            layoutParams.endToEnd = getDayId(scheduleInfo.rank ?: DAY_TYPE.MONDAY)
            textView.layoutParams = layoutParams

            binding.clSchedule.addView(textView)
        }
    }

    private fun getDayId(value: DAY_TYPE): Int {
        return when (value) {
            DAY_TYPE.MONDAY -> R.id.tvScheduleDay2
            DAY_TYPE.TUESDAY -> R.id.tvScheduleDay3
            DAY_TYPE.WEDNESDAY -> R.id.tvScheduleDay4
            DAY_TYPE.THURSDAY -> R.id.tvScheduleDay5
            DAY_TYPE.FRIDAY -> R.id.tvScheduleDay6
            DAY_TYPE.SATURDAY -> R.id.tvScheduleDay7
            DAY_TYPE.SUNDAY -> R.id.tvScheduleDay8
        }
    }

    private fun addIdHasMap() {
        hasMap[780] = R.id.glScheduleV1
        hasMap[840] = R.id.glScheduleV2
        hasMap[900] = R.id.glScheduleV3
        hasMap[960] = R.id.glScheduleV4
        hasMap[1020] = R.id.glScheduleV5
        hasMap[1080] = R.id.glScheduleV6
        hasMap[1140] = R.id.glScheduleV7
        hasMap[1200] = R.id.glScheduleV8
        hasMap[1260] = R.id.glScheduleV9
        hasMap[1320] = R.id.glScheduleV10
        hasMap[1380] = R.id.glScheduleV11
    }

    private fun addGuide(value: Int) {
        val guideline = Guideline(mainActivity)
        guideline.id = hasMap.size + 1
        val glLayoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        guideline.layoutParams = glLayoutParams
        (guideline.layoutParams as ConstraintLayout.LayoutParams).orientation = LinearLayout.HORIZONTAL
        (guideline.layoutParams as ConstraintLayout.LayoutParams).guidePercent = (((value / 60) - 13) * 0.0909 + (value % 60) * 0.0909 / 60 + 0.04545).toFloat()
        binding.clSchedule.addView(guideline)

        hasMap[value] = guideline.id
    }
}
