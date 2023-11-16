package vn.tutorme.mobile.presenter.home

import vn.tutorme.mobile.domain.model.banner.Banner

interface IHomeListener {
    fun onItemBannerClick(item: Banner)
    fun onClickTeachViewMore()
    fun onClickEvaluateViewMore()
    fun onClickClassRegisterViewMore()
    fun onClickClassWaitingConfirm()
    fun onClickConfirmRegisterClass(classId: String)
}
