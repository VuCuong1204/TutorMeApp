package vn.tutorme.mobile.domain.model.course

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.utils.TimeUtils
import vn.tutorme.mobile.utils.TimeUtils.DATE_FORMAT

@Parcelize
data class CourseInfo(
    var courseId: String? = null,
    var banner: String? = null,
    var title: String? = null,
    var ratingTotal: Float? = null,
    var content: String? = null,
    var subject: String? = null,
    var classCode: Int? = null,
    var ratingNumber: Int? = null,
    var memberRegister: Int? = null,
    var timeLesson: Long? = null,
    var createTime: Long? = null,
    var dateEnd: Long? = null,
    var price: Long? = null,
    var demoClass: Int? = null
) : IParcelable {
    fun getTimeLesson(): String {
        return TimeUtils.convertTimeToHourMinutes(timeLesson ?: Extension.LONG_DEFAULT)
    }

    fun getCountDemo(): String {
        return String.format(getAppString(R.string.class_count_demo), demoClass)
    }

    fun getMemberRegister(): String {
        return String.format(getAppString(R.string.joined), memberRegister)
    }

    fun getClassCodeInfo(): String {
        return String.format(getAppString(R.string.class_course), classCode)
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

    fun getCountRating(): String {
        return String.format(getAppString(R.string.count_rating_course), ratingNumber)
    }

    fun getTimeCreate(): String {
        return TimeUtils.convertTimeToDay(createTime ?: Extension.LONG_DEFAULT, DATE_FORMAT)
    }

    fun getTimeEnd(): String {
        return TimeUtils.convertTimeToDay(dateEnd ?: Extension.LONG_DEFAULT, DATE_FORMAT)
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
