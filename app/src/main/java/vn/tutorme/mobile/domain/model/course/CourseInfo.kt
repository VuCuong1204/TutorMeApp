package vn.tutorme.mobile.domain.model.course

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.utils.TimeUtils

@Parcelize
data class CourseInfo(
    var courseId: String? = null,
    var banner: String? = null,
    var title: String? = null,
    var ratingTotal: Float? = null,
    var ratingNumber: Int? = null,
    var memberRegister: Int? = null,
    var timeLesson: Long? = null,
    var dateEnd: Long? = null,
    var price: Long? = null
) : IParcelable {
    fun getTimeLesson(): String {
        return TimeUtils.convertTimeToHour(timeLesson ?: Extension.LONG_DEFAULT)
    }

    fun getTimeExpired(): String {
        return TimeUtils.convertTimeToDay(dateEnd ?: Extension.LONG_DEFAULT)
    }

    fun getCountPeopleRegister(): String {
        return String.format(getAppString(R.string.people_register), memberRegister)
    }

    fun getPriceValue(): String {
        return String.format(getAppString(R.string.price_value), price)
    }
}

fun mockDataCourseInfo(size: Int = 5): List<CourseInfo> {
    val list = mutableListOf<CourseInfo>()
    repeat(size) {
        list.add(CourseInfo(
            courseId = "${it}",
            price = 12400000,
            memberRegister = 124,
            title = "Giọng nói chuẩn Tiếng Anh trong sư phạm",
            banner = "https://diem10cong.edu.vn/image/catalog/hinhanh/khaigiangth2021.2022.jpg",
            ratingTotal = 4.7f,
            ratingNumber = 124
        ))
    }

    return list
}
