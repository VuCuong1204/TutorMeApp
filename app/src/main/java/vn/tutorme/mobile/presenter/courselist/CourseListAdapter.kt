package vn.tutorme.mobile.presenter.courselist

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.loadImage
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.CourseInfoItemBinding
import vn.tutorme.mobile.domain.model.course.CourseInfo

class CourseListAdapter : TutorMeAdapter() {

    var listener: ICourseListListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.course_info_item

    override fun getColumnInRow(viewType: Int): Int = 2

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return CourseListVH(binding as CourseInfoItemBinding)
    }

    inner class CourseListVH(private val binding: CourseInfoItemBinding) : BaseVH<CourseInfo>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem {
                    listener?.onItemClick(it)
                }
            }
        }

        override fun onBind(data: CourseInfo) {
            super.onBind(data)
            with(binding) {
                ivCourseInfoInfoBanner.loadImage(data.banner)
                tvCourseInfoTitle.text = data.title

                val text = SpannableBuilder()
                    .appendText("${data.ratingTotal} ")
                    .withSpanImage(getAppDrawable(R.drawable.ic_star))
                    .appendText(" ${String.format(getAppString(R.string.rating_count), data.ratingNumber)}")
                    .spannedText

                tvCourseInfoRate.text = text
                tvCourseInfoCountMember.text = data.getCountPeopleRegister()
                tvCourseInfoDate.text = data.getTimeExpired()
                tvCourseInfoPrice.text = data.getPriceValue()
            }
        }
    }
}
