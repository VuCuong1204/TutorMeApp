package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.lesson.TITLE_TYPE
import vn.tutorme.mobile.domain.model.lesson.TitleLessonInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetLessonListInClassUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetLessonListInClassUseCase.GetLessonListInClassVH, List<Any>>() {

    private val hashMap = hashMapOf<String, Boolean>()

    override suspend fun execute(rv: GetLessonListInClassVH): List<Any> {
        hashMap.clear()
        val list = mutableListOf<Any>()
        val lessonList = lessonRepo.getLessonListInClass(rv.classId, rv.page, rv.size)
        lessonList.forEach {
            if (!hashMap.containsKey(it.getDayBegin())) {
                list.add(TitleLessonInfo(it.getDayBegin(), TITLE_TYPE.TITLE_DAY_TYPE))
                hashMap[it.getDayBegin()] = true
            }
            list.add(it)
        }

        return list
    }

    class GetLessonListInClassVH(val classId: String) : RequestValue {
        var page: Int? = null
        var size: Int? = null
    }
}
