package vn.tutorme.mobile.domain.model.mission

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class MissionInfo(
    var timeBegin: Long? = null,
    var timeEnd: Long? = null,
    var numberCasesTaught: Int? = null,
    var totalCases: Int? = null,
    var numberSessionsEvaluate: Int? = null,
    var totalSessionsEvaluate: Int? = null,
) : IParcelable {
    fun getMissionState(): Boolean {
        return numberCasesTaught == totalCases && numberSessionsEvaluate == totalSessionsEvaluate
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
}

fun mockDataMissionInfo(): MissionInfo {
    return MissionInfo(
        numberCasesTaught = 20,
        totalCases = 20,
        numberSessionsEvaluate = 20,
        totalSessionsEvaluate = 20
    )
}
