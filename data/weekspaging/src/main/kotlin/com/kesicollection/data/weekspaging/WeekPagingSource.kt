package com.kesicollection.data.weekspaging

import com.kesicollection.core.model.Week
import com.kesicollection.data.weekspaging.model.PagingEvent
import com.kesicollection.data.weekspaging.usecase.WeekBuilderUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.OffsetDateTime
import javax.inject.Inject

interface WeekPagingSource {
    fun getPagingEvents(): Flow<PagingEvent<Week>>
    fun clear()
    fun startFrom(from: OffsetDateTime): PagingEvent<Week>
    suspend fun watch(index: Int)
}

internal class WeekPagingSourceImpl @Inject constructor(
    private val weekBuilderUseCase: WeekBuilderUseCase,
    private val weekPageConfig: WeekPageConfig,
) : WeekPagingSource {

    private var firstWeekStart: OffsetDateTime? = null
    private var finalWeekStart: OffsetDateTime? = null
    private var sizeTracker = 0

    private val _pagingEvent = MutableStateFlow<PagingEvent<Week>>(PagingEvent.Initial(Unit))
    private val pagingEvent: StateFlow<PagingEvent<Week>>
        get() = _pagingEvent

    override fun getPagingEvents(): Flow<PagingEvent<Week>> = pagingEvent

    override suspend fun watch(index: Int) {
        if (sizeTracker == 0) throw IllegalAccessException("watch function cannot be called before startFrom function")
        when (weekPageConfig.evaluatePageState(index, sizeTracker)) {
            PageState.Append -> {
                finalWeekStart?.let {
                    weekBuilderUseCase(
                        it.plusWeeks(1),
                        weekPageConfig.getPageSize().toLong()
                    ).apply {
                        sizeTracker += weekPageConfig.getPageSize()
                        finalWeekStart = this.last().days.first()
                        _pagingEvent.emit(
                            PagingEvent.Appended(
                                this,
                                weekPageConfig.getCalculatedIndex()
                            )
                        )
                    }
                }
            }

            PageState.Prepend -> {
                firstWeekStart?.let {
                    weekBuilderUseCase(
                        it.minusWeeks(weekPageConfig.getPageSize().toLong()),
                        weekPageConfig.getPageSize().toLong()
                    ).apply {
                        sizeTracker += weekPageConfig.getPageSize()
                        firstWeekStart = this.first().days.first()
                        _pagingEvent.emit(
                            PagingEvent.Prepended(
                                this,
                                weekPageConfig.getCalculatedIndex()
                            )
                        )
                    }
                }
            }

            PageState.Middle -> Unit /* no-op */
        }
    }

    override fun startFrom(from: OffsetDateTime): PagingEvent<Week> {
        val startedFrom = weekBuilderUseCase(
            from.minusWeeks(weekPageConfig.getPageSize().toLong() / 2),
            weekPageConfig.getPageSize().toLong()
        )
        firstWeekStart = startedFrom.first().days.first()
        finalWeekStart = startedFrom.last().days.first()
        sizeTracker = weekPageConfig.getPageSize()
        return PagingEvent.Appended(
            startedFrom,
            weekPageConfig.getCalculatedIndex()
        )
    }

    override fun clear() {
        finalWeekStart = null
        firstWeekStart = null
        sizeTracker = 0
    }

}