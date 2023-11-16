package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.authen.UserInfo

interface IAuthRepo {
    fun register(userId: String): UserInfo
    fun login(userId: String): UserInfo
    fun updateProfile(
        userId: String,
        fullName: String,
        date: String,
        address: String,
        nameSchool: String,
        gender: Int,
        phoneNumber: Long,
        avatar: String
    ): Boolean

    fun getAccessTokenVideo(id: String): String
}
