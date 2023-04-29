package org.appcenter.inudorm.util

import kotlinx.coroutines.flow.MutableStateFlow
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.presentation.matching.MutationEvent
import org.appcenter.inudorm.usecase.ResultUseCase
import kotlin.reflect.KClass
import kotlin.reflect.KProperty0
import kotlin.reflect.full.primaryConstructor

sealed class State<T> {
    class Initial<T> : State<T>()
    data class Success<T>(val data: T?) : State<T>()
    class Loading<T> : State<T>()
    data class Error<T>(val error: Throwable) : State<T>()

    fun isLoading() = this is Loading
    fun isError() = this is Error
    fun isSuccess() = this is Success

    companion object {
        suspend fun <P, R> fetch(
            state: MutableStateFlow<MutationEvent>,
            event: KClass<MutationEvent>,
            useCase: ResultUseCase<P, R>,
            param: P,
        ): MutableStateFlow<MutationEvent> {
            state.emit(event.primaryConstructor?.call(Mutation(param, Loading<R>()))!!)
            val result = useCase.run(param)
            state.emit(event.primaryConstructor?.call(Mutation(param, result))!!)
            return state
        }
    }
}