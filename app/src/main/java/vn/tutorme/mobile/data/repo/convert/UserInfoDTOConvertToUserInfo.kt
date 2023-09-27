package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.user.UserInfoDTO
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.authen.UserInfo

class UserInfoDTOConvertToUserInfo : IConverter<UserInfoDTO, UserInfo> {
    override fun convert(source: UserInfoDTO): UserInfo {
        val mapper = UserInfo().copy(
            userId = source.userId,
            fullName = source.fullName,
            date = source.date,
            address = source.address,
            nameSchool = source.nameSchool,
            gender = GENDER_TYPE.valuesOfName(source.gender) ?: GENDER_TYPE.OTHER,
            phoneNumber = source.phoneNumber,
            role = ROLE_TYPE.valuesOfName(source.role) ?: ROLE_TYPE.STUDENT_TYPE
        )

        return mapper
    }
}
