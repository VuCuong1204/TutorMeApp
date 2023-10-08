package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonAllStudentUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonAllStudentUseCase.GetLessonAllStudentVH, List<Any>>() {
    override suspend fun execute(rv: GetLessonAllStudentVH): List<Any> {

        val lessonAllList = mutableListOf<Any>()
        val lessonInfoList = lessonRepo.getLessonStudentInDay(rv.id, rv.beginTime, rv.endTime, null, null)

        lessonInfoList.forEach {
            lessonAllList.add(it.getTimeLearnHour())
            lessonAllList.add(it)
        }

        return lessonAllList
    }

    class GetLessonAllStudentVH(val id: String, var beginTime: Long, val endTime: Long) : RequestValue
}
