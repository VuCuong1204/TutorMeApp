package vn.tutorme.mobile.domain.model.lesson

enum class LESSON_STATUS(val value: Int) {
    UPCOMING_STATUS(0),
    HAPPENNING_STATUS(1),
    CANCEL_STATUS(2),
    TOOK_PLACE_STATUS(3);
}
