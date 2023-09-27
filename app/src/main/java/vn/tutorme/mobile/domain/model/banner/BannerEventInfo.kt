package vn.tutorme.mobile.domain.model.banner

data class BannerEventInfo(
    var id: Int? = null,
    var banner: String? = null,
    var title: String? = null,
    var createTime: Long? = null,
    var content: String? = null,
    var countRegister: Int? = null,
    var describe: String? = null,
    var joinInstructions: String? = null
)
