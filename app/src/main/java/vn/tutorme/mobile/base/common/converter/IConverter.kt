package vn.tutorme.mobile.base.common.converter

interface IConverter<S, D> {
    fun convert(source: S): D
}
