package vn.tutorme.mobile.domain.repo

interface IAuthRepo {
    fun register(userId: String)
    fun login(userId: String)
}
