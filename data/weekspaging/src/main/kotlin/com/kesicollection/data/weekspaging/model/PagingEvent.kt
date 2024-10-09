package com.kesicollection.data.weekspaging.model

sealed class PagingEvent<T> {
    data class Initial<T>(val unit: Unit) : PagingEvent<T>()
    data class Appended<T>(val newData: List<T>) : PagingEvent<T>()
    data class Prepended<T>(val newData: List<T>, val updatedComputedIndex: Int) : PagingEvent<T>()
}