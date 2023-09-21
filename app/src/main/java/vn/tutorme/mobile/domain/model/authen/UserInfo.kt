package vn.tutorme.mobile.domain.model.authen

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class UserInfo(

    var userId: String? = null,

    var fullName: String? = null,

    var date: String? = null,

    var address: String? = null,

    var nameSchool: String? = null,

    var gender: GENDER_TYPE? = null,

    var phoneNumber: Long? = null,

    var role: ROLE_TYPE? = null

) : IParcelable
