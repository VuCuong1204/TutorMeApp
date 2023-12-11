package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.user.UserInfoDTO
import vn.tutorme.mobile.domain.model.authen.ATTENDANCE_STATE
import vn.tutorme.mobile.domain.model.authen.EVALUATE_STATE
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.authen.UserInfo

class UserInfoDTOConvertToUserInfo : IConverter<UserInfoDTO, UserInfo> {
    override fun convert(source: UserInfoDTO): UserInfo {

        return UserInfo().copy(
            userId = source.userId,
            fullName = source.fullName,
            date = source.date,
            address = source.address,
            nameSchool = source.nameSchool,
            gender = GENDER_TYPE.valuesOfName(source.gender) ?: GENDER_TYPE.OTHER,
            phoneNumber = source.phoneNumber,
            avatar = source.avatar,
            role = ROLE_TYPE.valuesOfName(source.role) ?: ROLE_TYPE.STUDENT_TYPE,
            attendanceState = ATTENDANCE_STATE.valuesOfName(source.stateAttendance),
            evaluateState = EVALUATE_STATE.valuesOfName(source.updateReview),
            scoreAttitude = source.scoreAttitude,
            commentAttitude = source.commentAttitude,
            scorePreparation = source.scorePreparation,
            commentPreparation = source.commentPreparation,
            scoreAskQuestion = source.scoreAskQuestion,
            commentAskQuestion = source.commentAskQuestion,
            scoreJoinTheDiscussion = source.scoreJoinTheDiscussion,
            commentJoinTheDiscussion = source.commentJoinTheDiscussion,
            scoreAttention = source.scoreAttention,
            commentAttention = source.commentAttention,
            scoreCompleteTheXercise = source.scoreCompleteTheXercise,
            commentCompleteTheXercise = source.commentCompleteTheXercise,
            commentMedium = source.commentMedium
        )
    }
}
