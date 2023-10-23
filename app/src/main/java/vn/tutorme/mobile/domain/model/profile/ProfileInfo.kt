package vn.tutorme.mobile.domain.model.profile

import vn.tutorme.mobile.R

data class ProfileInfo(
    var id: Int? = null,
    var name: String? = null,
    var avatar: Int? = null,
    var type: PROFILE_TYPE? = null
)

fun mockDataProfileInfo(): List<ProfileInfo> {
    return listOf(
        ProfileInfo(0, "Thông tin\ntài khoản", R.drawable.ic_info_person, PROFILE_TYPE.INFO_TYPE),
        ProfileInfo(0, "Thông tin\nliên hệ", R.drawable.ic_info_contact_person, PROFILE_TYPE.INFO_CONTACT_TYPE),
        ProfileInfo(0, "Đổi mật khẩu", R.drawable.ic_change_password_person, PROFILE_TYPE.CHANGE_PASSWORD_TYPE),
        ProfileInfo(0, "Đăng xuất", R.drawable.ic_logout_person, PROFILE_TYPE.LOGOUT_TYPE)
    )
}
