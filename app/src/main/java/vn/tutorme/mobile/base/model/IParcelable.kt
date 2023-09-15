package vn.tutorme.mobile.base.model

import android.os.Parcelable

interface IParcelable : Parcelable, Cloneable {
    override fun describeContents(): Int {
        return 0
    }
}
