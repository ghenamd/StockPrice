package com.stockprice.utils

sealed class Response<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T?) : Response<T>(data)
    class Error<T>(error: String?) : Response<T>(data = null, error = error)
    class Loading<T>(val isLoading:Boolean) : Response<T>()
}
