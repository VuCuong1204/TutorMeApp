package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LESSON_TYPE
import vn.tutorme.mobile.domain.model.mission.MissionInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetMissionInfoUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetMissionInfoUseCase.GetMissionInfoRV, MissionInfo>() {
    override suspend fun execute(rv: GetMissionInfoRV): MissionInfo {
        val lessonInfoList = lessonRepo.getMissionInfo(rv.id, rv.beginTime, rv.endTime)
        var taughtCount = 0
        var evaluateCount = 0
        lessonInfoList.forEach {
            if (it.status == LESSON_STATUS.TOOK_PLACE_STATUS) taughtCount++
            if (it.type == LESSON_TYPE.NOT_YET_RATE_TYPE) evaluateCount++
        }

        return MissionInfo(
            numberCasesTaught = taughtCount,
            totalCases = lessonInfoList.size,
            numberSessionsEvaluate = evaluateCount,
            totalSessionsEvaluate = lessonInfoList.size,
        )
    }

    class GetMissionInfoRV(val id: String, var beginTime: Long, val endTime: Long) : RequestValue
}
