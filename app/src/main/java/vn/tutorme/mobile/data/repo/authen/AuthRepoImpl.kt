package vn.tutorme.mobile.data.repo.authen

import vn.tutorme.mobile.base.common.exception.ApiException
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.data.repo.convert.UserInfoDTOConvertToUserInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.base.invokeStringeeService
import vn.tutorme.mobile.data.source.remote.model.user.UserInfoRequest
import vn.tutorme.mobile.data.source.remote.service.IAuthService
import vn.tutorme.mobile.data.source.remote.service.IOtherService
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.repo.IAuthRepo
import javax.inject.Inject

class AuthRepoImpl @Inject constructor() : IRepo, IAuthRepo {
    override fun register(userId: String): UserInfo {
        val service = invokeAuthService(IAuthService::class.java)

        return service.register(userId).invokeApi { _, body ->
            body.data?.userInfo?.let { UserInfoDTOConvertToUserInfo().convert(it) }
        } ?: UserInfo()
    }

    override fun login(userId: String): UserInfo {
        val service = invokeAuthService(IAuthService::class.java)

        return service.login(userId).invokeApi { _, body ->
            body.data?.userInfo?.let { UserInfoDTOConvertToUserInfo().convert(it) }
        } ?: UserInfo()
    }

    override fun updateProfile(userId: String, fullName: String, date: String, address: String, nameSchool: String, gender: Int, phoneNumber: Long, avatar: String): Boolean {
        val service = invokeAuthService(IAuthService::class.java)

        val userInfoRequest = UserInfoRequest(
            userId = userId,
            fullName = fullName,
            date = date,
            address = address,
            nameSchool = nameSchool,
            gender = gender,
            phoneNumber = phoneNumber,
            avatar = avatar
        )

        return service.updateProfile(userInfoRequest).invokeApi { _, _ ->
            true
        }
    }

    override fun getAccessTokenVideo(id: String): String {
        val server = invokeStringeeService(IOtherService::class.java)
        val data = server.getAccessTokenVideoCall(id).execute()
        if (data.isSuccessful) {
            return data.body()?.accessToken ?: STRING_DEFAULT
        } else {
            throw ApiException(ApiException.TIME_OUT_ERROR)
        }
    }
}
