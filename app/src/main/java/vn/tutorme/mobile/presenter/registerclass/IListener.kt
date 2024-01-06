package vn.tutorme.mobile.presenter.registerclass

interface IListener {
    fun onConfirmClick(classId: String)
    fun onReceived(classId: String)
    fun onCancel(classId: String)
}
