package vn.tutorme.mobile.data.repo.authen

import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.service.IAuthService
import vn.tutorme.mobile.domain.repo.IAuthRepo

class AuthRepoImpl : IRepo, IAuthRepo {
    override fun register(userId: String) {
        val service = invokeAuthService(IAuthService::class.java)
    }

    override fun login(userId: String) {
        TODO("Not yet implemented")
    }
}
