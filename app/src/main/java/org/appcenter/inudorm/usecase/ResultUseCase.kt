package org.appcenter.inudorm.usecase

import io.sentry.Sentry
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State


abstract class ResultUseCase<P, R> {
    abstract suspend fun onExecute(params: P): R

    suspend fun run(params: P): State<R> {
        return run(params, null)
    }

    suspend fun run(params: P, mapper: ((R) -> R)? = null): State<R> {
        try {
            IDormLogger.d(this, "Running UseCase $this with following params: $params")
            return kotlin.runCatching {
                State.Success(
                    if (mapper == null) onExecute(params) else mapper(onExecute(params))
                )
            }.getOrElse {
                Sentry.captureException(it)
                IDormLogger.e(
                    this,
                    "Exception ${it.javaClass.canonicalName} occurred while running UseCase: $this\nDetail: ${it.stackTraceToString()}"
                )
                State.Error(it)
            }
        } catch (e: Exception) {
            Sentry.captureException(e)
            IDormLogger.e(
                this,
                "Exception ${e.javaClass.canonicalName} occurred while running UseCase: $this\nDetail: ${e.stackTraceToString()}"
            )
            throw e;
        }

    }

}