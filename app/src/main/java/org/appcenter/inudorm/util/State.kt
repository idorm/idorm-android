package org.appcenter.inudorm.util

sealed class State<T> {
    class Initial<T> : State<T>()
    data class Success<T>(val data: T?) : State<T>()
    class Loading<T> : State<T>()
    data class Error<T>(val error: Throwable) : State<T>()

    fun isLoading() = this is Loading
    fun isError() = this is Error
    fun isSuccess() = this is Success
}