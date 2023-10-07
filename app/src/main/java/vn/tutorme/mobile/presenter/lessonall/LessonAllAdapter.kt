package vn.tutorme.mobile.presenter.lessonall

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
import vn.tutorme.mobile.databinding.InfoItemBinding
import vn.tutorme.mobile.databinding.TitleTimeItemBinding
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LessonInfo

class LessonAllAdapter : TutorMeAdapter() {

    companion object {
        const val TITLE_VIEW_TYPE = 10
        const val CONTENT_VIEW_TYPE = 11
    }

    override fun getItemViewType(position: Int): Int {
        return when (getDataListAtPosition(position)) {
            is String -> {
                TITLE_VIEW_TYPE
            }

            is LessonInfo -> {
                CONTENT_VIEW_TYPE
            }

            else -> {
                super.getItemViewType(position)
            }
        }
    }

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TITLE_VIEW_TYPE -> {
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

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.lesson_all_empty)

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            TITLE_VIEW_TYPE -> {
                TitleVH(binding as TitleTimeItemBinding)
            }

            CONTENT_VIEW_TYPE -> {
                ContentVH(binding as InfoItemBinding)
            }

            else -> {
                null
            }
        }
    }

    inner class TitleVH(private val binding: TitleTimeItemBinding) : BaseVH<String>(binding) {
        override fun onBind(data: String) {
            super.onBind(data)
            binding.tvTitleTimeName.text = data
            if (absoluteAdapterPosition == 0) {
                binding.vTitleTimeV1.hide()
            }
        }
    }

    inner class ContentVH(private val binding: InfoItemBinding) : BaseVH<LessonInfo>(binding) {

        init {
            binding.tvInfoTimeSlot.gone()
            binding.tvInfoPencil.hide()
        }

        override fun onBind(data: LessonInfo) {
            super.onBind(data)
            with(binding) {
                tvInfoAvatar.setImageDrawable(
                    when (data.status) {
                        LESSON_STATUS.UPCOMING_STATUS -> getAppDrawable(R.drawable.ic_upcoming)
                        LESSON_STATUS.HAPPENING_STATUS -> getAppDrawable(R.drawable.ic_happenning)
                        LESSON_STATUS.CANCEL_STATUS -> getAppDrawable(R.drawable.ic_cancel)
                        LESSON_STATUS.TOOK_PLACE_STATUS -> getAppDrawable(R.drawable.ic_took_place)
                        else -> getAppDrawable(R.drawable.ic_upcoming)
                    }
                )
                tvInfoId.text = data.classId
                tvInfoState.text = when (data.status) {
                    LESSON_STATUS.UPCOMING_STATUS -> getAppString(R.string.upcoming)
                    LESSON_STATUS.HAPPENING_STATUS -> getAppString(R.string.happening)
                    LESSON_STATUS.CANCEL_STATUS -> getAppString(R.string.cancel)
                    LESSON_STATUS.TOOK_PLACE_STATUS -> getAppString(R.string.took_place)
                    else -> getAppString(R.string.upcoming)
                }
                tvInfoClass.text = data.getClassTitle()
                tvInfoAdvanced.text = data.level
                tvInfoNumber.text = data.getNumberMember()
                tvInfoLesson.text = data.getSession()

                if (absoluteAdapterPosition == dataList.lastIndex) {
                    val params = binding.clInfoRoot.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(
                        getAppDimension(R.dimen.fbase_dimen_18).toInt(),
                        0,
                        0,
                        getAppDimension(R.dimen.fbase_dimen_20).toInt()
                    )

                    binding.clInfoRoot.layoutParams = params
                }
            }
        }
    }
}
