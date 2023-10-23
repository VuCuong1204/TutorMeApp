package vn.tutorme.mobile.domain.model.banner

data class BannerJobInfo(
    var id: Int? = null,
    var banner: String? = null,
    var title: String? = null,
    var income: String? = null,
    var countMember: Int? = null,
    var deadlineRegister: Long? = null,
    var describe: String? = null,
    var requestJob: String? = null,
    var benefit: String? = null
)
