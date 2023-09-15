package com.example.basegraduate.presentation.widget.headeralert

import vn.tutorme.mobile.presention.widget.headeralert.HEADER_ALERT_TYPE

interface IHeaderAlert {
    fun show(msg: CharSequence?, type: HEADER_ALERT_TYPE)
    fun dismiss()
    fun destroy()
}
