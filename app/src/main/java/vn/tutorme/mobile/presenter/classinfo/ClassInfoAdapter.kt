package vn.tutorme.mobile.presenter.classinfo

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.hide
import vn.tutorme.mobile.base.extension.setImageTextView
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.InfoItemBinding
import vn.tutorme.mobile.databinding.TitleTimeItemBinding
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.lesson.TitleLessonInfo

class ClassInfoAdapter : TutorMeAdapter() {

    companion object {
        const val DAY_VIEW_TYPE = 10
        const val LESSON_VIEW_TYPE = 11
    }

    var listener: IClassInfoListener? = null

    override fun getItemViewType(position: Int): Int {
        return when (getDataListAtPosition(position)) {
            is TitleLessonInfo -> DAY_VIEW_TYPE
            is LessonInfo -> LESSON_VIEW_TYPE
            else -> super.getItemViewType(position)
        }
    }

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            DAY_VIEW_TYPE -> R.layout.title_time_item
            LESSON_VIEW_TYPE -> R.layout.info_item
            else -> LAYOUT_INVALID
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            DAY_VIEW_TYPE -> TitleVH(binding as TitleTimeItemBinding)
            LESSON_VIEW_TYPE -> LessonInfoVH(binding as InfoItemBinding)
            else -> null
        }
    }

    inner class TitleVH(private val binding: TitleTimeItemBinding) : BaseVH<TitleLessonInfo>(binding) {
        override fun onBind(data: TitleLessonInfo) {
            super.onBind(data)
            binding.tvTitleTimeName.text = data.name
            if (absoluteAdapterPosition == 0) {
                binding.vTitleTimeV1.hide()
            }
        }
    }

    inner class LessonInfoVH(private val binding: InfoItemBinding) : BaseVH<LessonInfo>(binding) {

        init {
            with(binding) {
                tvInfoPencil.hide()
                tvInfoAdvanced.hide()
                tvInfoTimeSlot.gone()

                root.setOnSafeClick {
                    getItem {
                        listener?.onItemClick(it)
                    }
                }
            }
        }

        override fun onBind(data: LessonInfo) {
            super.onBind(data)
            with(binding) {
                tvInfoId.text = data.getSession()
                tvInfoAvatar.setImageDrawable(
                    when (data.status) {
                        LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.ic_upcoming)
                        LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.ic_happenning)
                        LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.ic_cancel)
                        LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.ic_took_place)
                        else -> getAppDrawable(R.drawable.ic_upcoming)
                    }
                )

                tvInfoState.text = when (data.status) {
                    LESSON_STATUS.UPCOMING_STATUS -> getAppString(R.string.upcoming)
                    LESSON_STATUS.HAPPENING_STATUS -> getAppString(R.string.happening)
                    LESSON_STATUS.CANCEL_STATUS -> getAppString(R.string.cancel)
                    LESSON_STATUS.TOOK_PLACE_STATUS -> getAppString(R.string.took_place)
                    else -> getAppString(R.string.upcoming)
                }

                tvInfoState.background = when (data.status) {
                    LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.shape_bg_secondary_corner_8)
                    LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.shape_bg_status_manual_corner_10)
                    LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.shape_bg_classic_corner_10)
                    LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.shape_bg_state_sucess_corner_8)
                    else -> getAppDrawable(R.drawable.shape_bg_secondary_corner_8)
                }

                tvInfoClass.text = data.getClassTitle()
                tvInfoNumber.text = if (data.status == LESSON_STATUS.TOOK_PLACE_STATUS) {
                    data.getNumberMemberRatio()
                } else {
                    data.getNumberMember()
                }

                tvInfoLesson.setImageTextView(getAppDrawable(R.drawable.ic_clock))
                tvInfoLesson.text = data.getTimeLearnHour()

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
