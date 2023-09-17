package com.example.basegraduate.presentation.widget.headeralert

import vn.tutorme.mobile.presenter.widget.headeralert.HEADER_ALERT_TYPE

data class MessageInfo(
    var type: HEADER_ALERT_TYPE? = null,
    var msg: CharSequence? = null
) {
    fun isSame(msg: CharSequence?): Boolean {
        return this.msg == msg
    }
}
