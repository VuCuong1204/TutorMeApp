package vn.tutorme.mobile.domain.usecase

import vn.tutorme.mobile.domain.repo.IUserRepo
import vn.tutorme.mobile.base.common.BaseUseCase
import javax.inject.Inject

class GetUserInfoUseCase1 @Inject constructor(
    private val userRepo: IUserRepo
) : BaseUseCase<BaseUseCase.VoidRequest, String>() {
    override suspend fun execute(rv: VoidRequest): String {
        return userRepo.getOld()
    }
}
