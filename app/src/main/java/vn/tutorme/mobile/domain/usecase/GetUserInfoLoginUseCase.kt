package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.repo.IAuthRepo
import javax.inject.Inject

class GetUserInfoLoginUseCase @Inject constructor(
    private val authRepo: IAuthRepo
) : BaseUseCase<GetUserInfoLoginUseCase.GetUserInfoLoginRV, UserInfo>() {

    override suspend fun execute(rv: GetUserInfoLoginRV): UserInfo {
        val userInfo = authRepo.login(rv.id)
        AppPreferences.userInfo = userInfo
        return userInfo
    }

    class GetUserInfoLoginRV(val id: String) : RequestValue
}
