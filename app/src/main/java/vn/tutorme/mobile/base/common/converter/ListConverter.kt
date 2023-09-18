package vn.tutorme.mobile.base.common.converter

class ListConverter<S, D>(private val converter: IConverter<S, D>) {
    fun invoke(source: List<S>): List<D> {
        val result = mutableListOf<D>()
        source.forEach {
            result.add(converter.convert(it))
        }

        return result
    }
}
