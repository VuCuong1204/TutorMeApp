package vn.tutorme.mobile.base.adapter.group

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import vn.tutorme.mobile.base.adapter.BaseAdapter

class GroupManager {

    private val groupAdapter by lazy {
        ConcatAdapter(ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build())
    }

    private val groupList = mutableListOf<BaseAdapter>()

    val spanSizeList = mutableListOf<Int>()

    fun addAdapter(adapter: BaseAdapter) {
        groupAdapter.addAdapter(adapter)
        groupList.add(adapter)
        spanSizeList.addAll(adapter.getColumnInRowList())
    }

    fun getAdapter(): RecyclerView.Adapter<*> {
        return groupAdapter
    }

    fun getViewTypeFromGlobalPosition(globalIndex: Int): Int {
        return groupAdapter.getItemViewType(globalIndex)
    }

    fun getChildAdapterFromGlobalPosition(globalIndex: Int): BaseAdapter? {
        var result = 0
        groupList.forEachIndexed { index, baseAdapter ->
            result += baseAdapter.itemCount
            if (result - 1 >= globalIndex) {
                return groupList[index]
            }
        }

        return null
    }

    fun findLCMViewType(list: MutableList<Int>): Int {
        val childList = mutableListOf<Int>()
        for (i in list.indices step 2) {
            val valueStep = if (i + 1 == list.size) 1 else list[i + 1]
            val result = findLCM(list[i], valueStep)
            childList.add(result)
        }

        return if (childList.size == 1) childList[0] else findLCMViewType(childList)
    }

    fun getAdapterGroupFinal(): BaseAdapter? {
        return groupList.lastOrNull()
    }

    private fun findGCD(a: Int, b: Int): Int {
        return if (b == 0) a else findGCD(b, a % b)
    }

    private fun findLCM(a: Int, b: Int): Int {
        return a * b / findGCD(a, b)
    }
}
