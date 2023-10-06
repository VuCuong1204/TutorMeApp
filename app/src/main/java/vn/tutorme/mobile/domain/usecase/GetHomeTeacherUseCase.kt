package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.LIMIT_SIZE
import vn.tutorme.mobile.domain.model.banner.BANNER_TYPE
import vn.tutorme.mobile.domain.model.banner.Banner
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.domain.model.lesson.LESSON_TYPE
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.model.mission.MissionInfo
import vn.tutorme.mobile.domain.repo.IBannerRepo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import vn.tutorme.mobile.presenter.home.TITLE_HOME_TYPE
import javax.inject.Inject

class GetHomeTeacherUseCase @Inject constructor(
    private val bannerRepo: IBannerRepo,
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetHomeTeacherUseCase.GetHomeTeacherRV, List<Any>>() {
    override suspend fun execute(rv: GetHomeTeacherRV): List<Any> {

        val dataList = mutableListOf<Any>()

        val bannerEvent = bannerRepo.getBannerEvent(rv.page, rv.sizeEvaluator)
        val bannerJob = bannerRepo.getBannerJob(rv.page, rv.sizeEvaluator)

        val bannerList = mutableListOf<Banner>()

        bannerJob.forEachIndexed { index, bannerJobInfo ->
            if (index == 3) return@forEachIndexed
            bannerList.add(Banner(
                id = bannerJobInfo.id,
                link = bannerJobInfo.banner,
                type = BANNER_TYPE.JOB_TYPE,
            ))
        }

        bannerEvent.forEachIndexed { index, bannerEventInfo ->
            if (index == 2) return@forEachIndexed
            bannerList.add(Banner(
                id = bannerEventInfo.id,
                link = bannerEventInfo.banner,
                type = BANNER_TYPE.EVENT_TYPE,
            ))
        }

        dataList.add(bannerList)

        val lessonInfoList = lessonRepo.getMissionInfo(rv.teacherId, rv.beginTimeEvaluated, rv.endTimeEvaluated)
        var taughtCount = 0
        var evaluateCount = 0
        lessonInfoList.forEach {
            if (it.status == LESSON_STATUS.TOOK_PLACE_STATUS) taughtCount++
            if (it.type == LESSON_TYPE.NOT_YET_RATE_TYPE) evaluateCount++
        }

        dataList.add(TITLE_HOME_TYPE.MISSION_TYPE)
        val missionInfo = MissionInfo(
            numberCasesTaught = taughtCount,
            totalCases = lessonInfoList.size,
            numberSessionsEvaluate = evaluateCount,
            totalSessionsEvaluate = lessonInfoList.size,
        )

        dataList.add(missionInfo)

        val lessonScheduleList = lessonRepo.getLessonInfoTeacher(
            rv.teacherId,
            rv.beginTimeSchedule,
            rv.endTimeSchedule,
            null,
            rv.page,
            rv.sizeSchedule
        )
        val lessonScheduleNew = mutableListOf<LessonInfo>()
        lessonScheduleList.forEach {
            lessonScheduleNew.add(it.copy(type = null))
        }

        if (lessonScheduleNew.isEmpty()) lessonScheduleNew.add(LessonInfo())
        dataList.add(TITLE_HOME_TYPE.SCHEDULE_TYPE)
        dataList.add(lessonScheduleNew)

        dataList.add(TITLE_HOME_TYPE.LESSON_EVALUATE_TYPE)
        val lessonEvaluated = lessonRepo.getLessonInfoTeacher(
            rv.teacherId,
            rv.beginTimeEvaluated,
            rv.endTimeEvaluated,
            rv.stateRate.value,
            rv.page,
            rv.sizeEvaluator
        ).toMutableList()

        if (lessonEvaluated.isEmpty()) lessonEvaluated.add(LessonInfo().copy(type = LESSON_TYPE.NOT_YET_RATE_TYPE))
        dataList.add(lessonEvaluated)

        dataList.add(TITLE_HOME_TYPE.CLASS_WAITING_CONFIRM)
        val classInfoList = lessonRepo.getClassInfoRegisterTeach(
            rv.currentTime,
            rv.stateClass.value
        ).toMutableList()

        if (classInfoList.isEmpty()) classInfoList.add(ClassInfo())
        dataList.add(classInfoList)

        return dataList
    }

    class GetHomeTeacherRV(
        val teacherId: String
    ) : RequestValue {
        var page: Int? = INT_DEFAULT
        var sizeSchedule: Int? = LIMIT_SIZE
        var sizeEvaluator: Int? = LIMIT_SIZE
        var beginTimeSchedule: Long = 0
        var endTimeSchedule: Long = 0
        var beginTimeEvaluated: Long = 0
        var endTimeEvaluated: Long = 0
        var currentTime: Long? = null
        var stateRate = LESSON_TYPE.NOT_YET_RATE_TYPE
        var stateClass: CLASS_STATUS = CLASS_STATUS.EMPTY_CLASS_STATUS
    }
}
