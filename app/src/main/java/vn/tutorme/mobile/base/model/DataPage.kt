package vn.tutorme.mobile.base.model

class DataPage<DATA> {
    var page = 0
    var limitPage = 20
    var dataList: MutableList<DATA> = mutableListOf()

    companion object {
        fun <DATA> newInstance(dataPage: DataPage<DATA>?, isReload: Boolean = true): DataPage<DATA> {
            var newDataPage = DataPage<DATA>()
            if (isReload) {
                return newDataPage
            }

            if (dataPage != null) {
                newDataPage = dataPage
            }

            return newDataPage
        }
    }

    fun addAllDataList(list: List<DATA>?) {
        list?.let {
            this.page++
            this.dataList.addAll(list)
        }
    }

    fun addData(data: DATA) {
        this.dataList.add(data)
    }

    fun clearDataPage() {
        dataList.clear()
        page = 0
    }
}
