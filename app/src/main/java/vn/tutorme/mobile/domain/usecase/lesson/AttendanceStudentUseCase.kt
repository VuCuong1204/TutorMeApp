package vn.tutorme.mobile.domain.usecase.lesson

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.ILessonRepo
import javax.inject.Inject

class AttendanceStudentUseCase @Inject constructor(
    private val lessonRepo: ILessonRepo
) : BaseUseCase<AttendanceStudentUseCase.AttendanceStudentRV, Boolean>() {
    override suspend fun execute(rv: AttendanceStudentRV): Boolean {
        return lessonRepo.attendanceStudent(rv.lessonId, rv.studentId)
    }

    class AttendanceStudentRV(val lessonId: Int, val studentId: String) : RequestValue
}
