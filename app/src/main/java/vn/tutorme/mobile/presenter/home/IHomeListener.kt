package vn.tutorme.mobile.presenter.home

interface IHomeListener {
    fun onClickTeachViewMore()
    fun onClickEvaluateViewMore()
    fun onClickClassRegisterViewMore()
    fun onClickClassWaitingConfirm()
    fun onClickConfirmRegisterClass(classId: String)
}
