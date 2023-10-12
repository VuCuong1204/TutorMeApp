package vn.tutorme.mobile.domain.model.lesson

data class TitleLessonInfo(
    var name: String? = null,
    var type: TITLE_TYPE? = null
)

enum class TITLE_TYPE {
    TITLE_DAY_TYPE,
    TITLE_HOUR_TYPE
}
