package vn.tutorme.mobile.domain.model.banner

data class Banner(
    var id: Int? = null,
    var link: String? = null,
    var type: BANNER_TYPE? = null
)

fun mockDataBanner(size: Int = 5): List<Banner> {
    val dataList = mutableListOf<Banner>()
    repeat(size) {
        dataList.add(Banner(it, "https://e2.com.vn/wp-content/uploads/2022/05/chuong-trinh-he-2022-landing-page-banner-desktop-1.png", BANNER_TYPE.EVENT_TYPE))
    }

    return dataList
}
