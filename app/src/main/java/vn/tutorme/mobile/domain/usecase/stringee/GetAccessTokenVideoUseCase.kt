package vn.tutorme.mobile.domain.usecase.stringee

import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.domain.repo.IAuthRepo
import javax.inject.Inject

class GetAccessTokenVideoUseCase @Inject constructor(
    private val repo: IAuthRepo
) : BaseUseCase<GetAccessTokenVideoUseCase.GetAccessTokenVideoRV, String>() {
    override suspend fun execute(rv: GetAccessTokenVideoRV): String {
        return repo.getAccessTokenVideo(rv.id)
    }

    class GetAccessTokenVideoRV(val id: String) : RequestValue
}
