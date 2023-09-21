package vn.tutorme.mobile.base.common.anim

interface IScreenAnim {
    fun enter(): Int
    fun exit(): Int
    fun popEnter(): Int
    fun popExit(): Int
}
