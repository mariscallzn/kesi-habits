package com.kesicollection.data.weekspaging

internal interface WeekPageConfig {
    fun getPageSize(): Int
    fun getCalculatedIndex(index: Int? = null): Int
    fun evaluatePageState(index: Int, sizeTracker: Int): PageState
    fun clear()
}

internal class WeekPageConfigImpl(
    private val pageSize: Int,
    private val fetchDistance: Int,
) : WeekPageConfig {
    private var _calculatedIndex = pageSize / 2

    override fun getPageSize(): Int = pageSize

    override fun getCalculatedIndex(index: Int?): Int = index?.let {
        it + _calculatedIndex
    } ?: _calculatedIndex

    override fun evaluatePageState(index: Int, sizeTracker: Int): PageState =
        if ((index + _calculatedIndex) <= fetchDistance) {
            _calculatedIndex += pageSize
            PageState.Prepend
        } else if ((index + _calculatedIndex) >= sizeTracker - 1) {
            PageState.Append
        } else PageState.Middle

    override fun clear() {
        _calculatedIndex = pageSize / 2
    }

}

internal sealed class PageState {
    data object Append : PageState()
    data object Prepend : PageState()
    data object Middle : PageState()
}