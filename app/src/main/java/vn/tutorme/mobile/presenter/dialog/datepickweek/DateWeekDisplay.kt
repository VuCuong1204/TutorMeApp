package vn.tutorme.mobile.presenter.dialog.datepickweek

data class DateWeekDisplay(
    var beginTime: Long? = null,
    var endTime: Long? = null,
    var selected: Boolean? = null
) {
    fun getBeginTimeWeekText(): String {
        return ""
    }

    fun getEndTimeWeekText(): String {
        return ""
    }
}
