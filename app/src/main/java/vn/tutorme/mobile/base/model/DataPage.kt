package vn.tutorme.mobile.base.model

class DataPage<DATA> {
    var page = 0
    var limitPage = 10
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

    fun replaceDataList(list: List<DATA>) {
        this.dataList.clear()
        this.dataList.addAll(list)
    }

    fun clearDataPage() {
        dataList.clear()
        page = 0
    }

    fun hasLoadMore(): Boolean {
        return dataList.size - limitPage * (page + 1) >= 0
    }
}
