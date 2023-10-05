package vn.tutorme.mobile.domain.model.mission

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.model.IParcelable
import vn.tutorme.mobile.utils.TimeUtils

@Parcelize
data class MissionInfo(
    var timeBegin: Long? = null,
    var timeEnd: Long? = null,
    var numberCasesTaught: Int? = null,
    var totalCases: Int? = null,
    var numberSessionsEvaluate: Int? = null,
    var totalSessionsEvaluate: Int? = null,
) : IParcelable {
    fun getMissionCasesState(): Boolean {
        return numberCasesTaught == totalCases
    }

    fun getMissionSessionsEvaluateState(): Boolean {
        return numberSessionsEvaluate == totalSessionsEvaluate
    }

    fun getCasesState(): Boolean {
        return numberCasesTaught == totalCases
    }

    fun getSessionsState(): Boolean {
        return numberSessionsEvaluate == totalSessionsEvaluate
    }

    fun getTimeWeek(): String {
        return "02.10.2023 - 09.10.2023"
    }

    fun getHourBegin(): String {
        return TimeUtils.convertTimeToHour(timeBegin ?: LONG_DEFAULT)
    }

    fun getHourEnd(): String {
        return TimeUtils.convertTimeToHour(timeEnd ?: LONG_DEFAULT)
    }

    fun getDayBegin(): String {
        return TimeUtils.convertTimeToDay(timeBegin ?: LONG_DEFAULT)
    }

    fun getDayEnd(): String {
        return TimeUtils.convertTimeToDay(timeEnd ?: LONG_DEFAULT)
    }
}

fun mockDataMissionInfo(): MissionInfo {
    return MissionInfo(
        numberCasesTaught = 20,
        totalCases = 20,
        numberSessionsEvaluate = 20,
        totalSessionsEvaluate = 20
    )
}
