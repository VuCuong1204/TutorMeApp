package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.INT_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.LIMIT_SIZE
import vn.tutorme.mobile.domain.model.banner.BANNER_TYPE
import vn.tutorme.mobile.domain.model.banner.Banner
import vn.tutorme.mobile.domain.model.lesson.LessonInfo
import vn.tutorme.mobile.domain.repo.IBannerRepo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import vn.tutorme.mobile.presenter.home.TITLE_HOME_TYPE
import javax.inject.Inject

class GetHomeStudentUseCase @Inject constructor(
    private val bannerRepo: IBannerRepo,
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetHomeStudentUseCase.GetHomeTeacherRV, List<Any>>() {

    override suspend fun execute(rv: GetHomeTeacherRV): List<Any> {
        val dataList = mutableListOf<Any>()

        val bannerEvent = bannerRepo.getBannerEvent(rv.page, rv.size)
        val bannerJob = bannerRepo.getBannerJob(rv.page, rv.size)

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
        dataList.add(TITLE_HOME_TYPE.LEARN_TODAY_TYPE)
        val lessonInfoList = lessonRepo.getLessonStudentInDay(rv.studentId, rv.beginTime, rv.endTime, rv.page, rv.size)
        val lessonInfoNew = mutableListOf<LessonInfo>()
        lessonInfoList.forEach {
            lessonInfoNew.add(it.copy(type = null))
        }
        dataList.add(lessonInfoNew)

        val classInfoList = lessonRepo.getClassStudentRegistered(rv.studentId, rv.page, rv.size)
        dataList.add(TITLE_HOME_TYPE.CLASS_REGISTER_CONFIRM_TYPE)
        dataList.add(classInfoList)

        val courseInfoList = lessonRepo.getCourseList(rv.currentTime, rv.page, rv.size)
        dataList.add(TITLE_HOME_TYPE.LIST_COURSE_TYPE)
        dataList.addAll(courseInfoList)

        return dataList
    }

    class GetHomeTeacherRV(
        val studentId: String,
        val currentTime: Long,
        val beginTime: Long,
        val endTime: Long
    ) : RequestValue {
        var page: Int? = INT_DEFAULT
        var size: Int? = LIMIT_SIZE
    }
}
