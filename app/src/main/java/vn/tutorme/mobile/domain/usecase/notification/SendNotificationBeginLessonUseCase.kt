package vn.tutorme.mobile.domain.usecase.notification

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class SendNotificationBeginLessonUseCase @Inject constructor(
    private val notificationRepo: INotificationRepo
) : BaseUseCase<SendNotificationBeginLessonUseCase.SendNotificationBeginLessonRV, Boolean>() {
    override suspend fun execute(rv: SendNotificationBeginLessonRV): Boolean {
        return notificationRepo.sendNotificationBeginLesson(
            rv.tokens, rv.lessonId, rv.classId, rv.title, rv.body
        )
    }

    class SendNotificationBeginLessonRV(
        val tokens: String,
        val lessonId: String,
        val classId: String,
        val title: String,
        val body: String
    ) : RequestValue
}
