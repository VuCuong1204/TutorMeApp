package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonAllUserCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonAllUserCase.GetLessonAllRV, List<Any>>() {
    override suspend fun execute(rv: GetLessonAllRV): List<Any> {

        val lessonAllList = mutableListOf<Any>()
        val lessonInfoList = lessonRepo.getLessonInfoTeacher(rv.id, rv.beginTime, rv.endTime, null, null, null)

        lessonInfoList.forEach {
            lessonAllList.add(it.getTimeLearnHour())
            lessonAllList.add(it)
        }

        return lessonAllList
    }

    class GetLessonAllRV(val id: String, var beginTime: Long, val endTime: Long) : RequestValue
}
