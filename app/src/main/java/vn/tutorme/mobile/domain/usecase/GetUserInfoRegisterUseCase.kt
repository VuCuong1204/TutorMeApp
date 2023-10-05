package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.repo.IAuthRepo
import javax.inject.Inject

class GetUserInfoRegisterUseCase @Inject constructor(
    private val authRepo: IAuthRepo
) : BaseUseCase<GetUserInfoRegisterUseCase.GetUserInfoRegisterRV, UserInfo>() {

    override suspend fun execute(rv: GetUserInfoRegisterRV): UserInfo {
        val userInfo = authRepo.register(rv.id)
        AppPreferences.userInfo = userInfo
        AppPreferences.userNameAccount = STRING_DEFAULT
        AppPreferences.passwordAccount = STRING_DEFAULT
        AppPreferences.checkSaveInfo = false
        return userInfo
    }

    class GetUserInfoRegisterRV(val id: String) : RequestValue
}
