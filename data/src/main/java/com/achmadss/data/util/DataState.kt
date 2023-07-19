package com.achmadss.data.util

sealed class DataState<T>(
    val data: T?,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : DataState<T>(data)
    class Loading<T>(data: T? = null): DataState<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null): DataState<T>(data, throwable)
}