package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.authen.UserInfo

interface IAuthRepo {
    fun register(userId: String): UserInfo
    fun login(userId: String): UserInfo
}
