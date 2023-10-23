package vn.tutorme.mobile.domain.usecase.profile

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.base.common.BaseUseCase
import vn.tutorme.mobile.base.extension.Extension.LONG_DEFAULT
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.repo.IAuthRepo
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val authRepo: IAuthRepo
) : BaseUseCase<UpdateProfileUseCase.UpdateProfileRV, Boolean>() {

    override suspend fun execute(rv: UpdateProfileRV): Boolean {

        val updateState = authRepo.updateProfile(
            userId = "XoY3l0swNWQtLEOJCcZOC516d1u1",
            fullName = rv.fullName,
            date = rv.date,
            address = rv.address,
            nameSchool = rv.nameSchool,
            gender = rv.gender.value,
            phoneNumber = rv.phoneNumber,
            avatar = rv.avatar
        )

        val userId = AppPreferences.userInfo?.userId
        val role = AppPreferences.userInfo?.role

        AppPreferences.userInfo = UserInfo(
            userId = userId,
            fullName = rv.fullName,
            date = rv.date,
            address = rv.address,
            nameSchool = rv.nameSchool,
            gender = rv.gender,
            phoneNumber = rv.phoneNumber,
            avatar = rv.avatar,
            role = role
        )

        return updateState
    }

    class UpdateProfileRV : RequestValue {
        var fullName: String = STRING_DEFAULT
        var date: String = STRING_DEFAULT
        var address: String = STRING_DEFAULT
        var nameSchool: String = STRING_DEFAULT
        var gender: GENDER_TYPE = GENDER_TYPE.OTHER
        var phoneNumber: Long = LONG_DEFAULT
        var avatar: String = STRING_DEFAULT
    }
}
