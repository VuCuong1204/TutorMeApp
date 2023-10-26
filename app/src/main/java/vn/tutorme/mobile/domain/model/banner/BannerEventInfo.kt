package vn.tutorme.mobile.domain.model.banner

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.utils.TimeUtils

data class BannerEventInfo(
    var id: Int? = null,
    var banner: String? = null,
    var title: String? = null,
    var createTime: Long? = null,
    var content: String? = null,
    var countRegister: Int? = null,
    var describe: String? = null,
    var joinInstructions: String? = null
) {
    fun getTimeBegin(): String {
        return TimeUtils.convertTimeToDay(createTime ?: Extension.LONG_DEFAULT, TimeUtils.DATE_FORMAT)
    }

    fun getCountPeopleRegister(): String {
        return String.format(getAppString(R.string.people_register), countRegister)
    }
}
