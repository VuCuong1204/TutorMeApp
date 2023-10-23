package vn.tutorme.mobile.presenter.profile

import vn.tutorme.mobile.domain.model.profile.ProfileInfo

interface IProfileListener {
    fun onItemClick(item : ProfileInfo)
}
