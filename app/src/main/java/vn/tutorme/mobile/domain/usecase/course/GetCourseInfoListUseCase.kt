package vn.tutorme.mobile.domain.usecase.course

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class GetCourseInfoListUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<GetCourseInfoListUseCase.GetCourseInfoListRV, List<CourseInfo>>() {
    override suspend fun execute(rv: GetCourseInfoListRV): List<CourseInfo> {
        return lessonRepo.getCourseList(rv.currentTime, rv.page, rv.size)
    }

    class GetCourseInfoListRV(val currentTime: Long) : RequestValue {
        val page: Int? = null
        val size: Int? = null
    }
}
