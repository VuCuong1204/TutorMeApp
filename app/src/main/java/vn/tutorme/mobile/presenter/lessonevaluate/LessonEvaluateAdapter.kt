package vn.tutorme.mobile.presenter.lessonevaluate

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.hide
import vn.tutorme.mobile.databinding.InfoItemBinding
import vn.tutorme.mobile.databinding.TitleDayItemBinding
import vn.tutorme.mobile.databinding.TitleTimeItemBinding
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.lesson.TITLE_TYPE
import vn.tutorme.mobile.domain.model.lesson.TitleLessonInfo

class LessonEvaluateAdapter : TutorMeAdapter() {

    companion object {
        const val TITLE_DAY_VIEW_TYPE = 10
        const val TITLE_HOUR_VIEW_TYPE = 11
        const val CONTENT_VIEW_TYPE = 12
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getDataListAtPosition(position)) {
            is TitleLessonInfo -> {
                if (item.type == TITLE_TYPE.TITLE_DAY_TYPE) {
                    TITLE_DAY_VIEW_TYPE
                } else {
                    TITLE_HOUR_VIEW_TYPE
                }
            }

            is LessonInfo -> {
                CONTENT_VIEW_TYPE
            }

            else -> {
                super.getItemViewType(position)
            }
        }
    }

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.lesson_evaluate_empty)

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TITLE_DAY_VIEW_TYPE -> {
                R.layout.title_day_item
            }

            TITLE_HOUR_VIEW_TYPE -> {
                R.layout.title_time_item
            }

            CONTENT_VIEW_TYPE -> {
                R.layout.info_item
            }

            else -> {
                LAYOUT_INVALID
            }
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            TITLE_DAY_VIEW_TYPE -> TitleDayVH(binding as TitleDayItemBinding)
            TITLE_HOUR_VIEW_TYPE -> TitleHourVH(binding as TitleTimeItemBinding)
            CONTENT_VIEW_TYPE -> LessonEvaluateVH(binding as InfoItemBinding)
            else -> null
        }
    }

    inner class TitleDayVH(private val binding: TitleDayItemBinding) : BaseVH<TitleLessonInfo>(binding) {
        override fun onBind(data: TitleLessonInfo) {
            super.onBind(data)

            binding.tvTitleDayName.text = data.name
        }
    }

    inner class TitleHourVH(private val binding: TitleTimeItemBinding) : BaseVH<TitleLessonInfo>(binding) {
        override fun onBind(data: TitleLessonInfo) {
            super.onBind(data)

            binding.tvTitleTimeName.text = data.name
            if (absoluteAdapterPosition > 0 &&
                getDataListAtPosition(absoluteAdapterPosition - 1) is TitleLessonInfo &&
                (getDataListAtPosition(absoluteAdapterPosition - 1) as TitleLessonInfo).type == TITLE_TYPE.TITLE_DAY_TYPE
            ) {
                binding.vTitleTimeV1.hide()
            }
        }
    }

    inner class LessonEvaluateVH(private val binding: InfoItemBinding) : BaseVH<LessonInfo>(binding) {

        override fun onBind(data: LessonInfo) {
            super.onBind(data)

            with(binding) {
                tvInfoTimeSlot.text = data.getNumberEvaluate()
                tvInfoAvatar.setImageDrawable(getAppDrawable(R.drawable.ic_took_place))
                tvInfoAvatar.background = (getAppDrawable(R.drawable.shape_bg_state_sucess_corner_8))
                tvInfoId.text = data.classId
                tvInfoState.text = getAppString(R.string.took_place)
                tvInfoClass.text = data.getClassTitle()
                tvInfoAdvanced.text = data.level
                tvInfoNumber.text = data.getNumberMemberRatio()
                tvInfoLesson.text = data.getSession()

                if (absoluteAdapterPosition == dataList.lastIndex) {
                    val params = clInfoRoot.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(
                        getAppDimension(R.dimen.fbase_dimen_18).toInt(),
                        0,
                        0,
                        getAppDimension(R.dimen.fbase_dimen_20).toInt()
                    )

                    clInfoRoot.layoutParams = params
                }
            }
        }
    }
}
