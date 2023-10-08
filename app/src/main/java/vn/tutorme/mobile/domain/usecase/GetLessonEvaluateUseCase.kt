package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.lesson.LESSON_TYPE
import vn.tutorme.mobile.domain.model.lesson.TITLE_TYPE
import vn.tutorme.mobile.domain.model.lesson.TitleLessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonEvaluateUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonEvaluateUseCase.GetLessonEvaluateVH, List<Any>>() {

    private val hashmap = hashMapOf<String, Boolean>()

    override suspend fun execute(rv: GetLessonEvaluateVH): List<Any> {
        val list = mutableListOf<Any>()
        val lessonInfoList = lessonRepo.getLessonInfoTeacher(
            rv.id,
            rv.beginTime,
            rv.endTime,
            LESSON_TYPE.NOT_YET_RATE_TYPE.value,
            null,
            null
        )

        lessonInfoList.forEach {
            if (!hashmap.containsKey(it.getDayBegin())) {
                list.add(TitleLessonInfo(it.getDayBegin(), TITLE_TYPE.TITLE_DAY_TYPE))
                hashmap[it.getDayBegin()] = true
            }
            list.add(TitleLessonInfo(it.getTimeLearnHour(), TITLE_TYPE.TITLE_HOUR_TYPE))
            list.add(it)
        }

        return list
    }

    class GetLessonEvaluateVH(val id: String, var beginTime: Long, val endTime: Long) : RequestValue
}
