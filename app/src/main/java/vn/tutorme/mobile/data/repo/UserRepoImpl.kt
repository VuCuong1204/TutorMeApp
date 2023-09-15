package vn.tutorme.mobile.data.repo

import vn.tutorme.mobile.domain.repo.IUserRepo
import javax.inject.Inject

class UserRepoImpl @Inject constructor() : IUserRepo {
    override fun getName(): String = "hello"
    override fun getOld(): String = "2023"
}
