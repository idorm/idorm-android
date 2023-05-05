package org.appcenter.inudorm.usecase

import io.sentry.Sentry
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State


abstract class ResultUseCase<P, R> {
    abstract suspend fun onExecute(params: P): R

    suspend fun run(params: P): State<R> {
        try {
            IDormLogger.d(this, "Running UseCase $this with following params: $params")
            return kotlin.runCatching {
                State.Success(onExecute(params))
            }.getOrElse {
                Sentry.captureException(it)
                IDormLogger.e(
                    this,
                    "Exception ${it.javaClass.canonicalName} occurred while running UseCase: $this\nDetail: ${it.stackTraceToString()}"
                )
                State.Error(it)
            }
        } catch (e: Exception) {
            IDormLogger.e(
                this,
                "Exception ${e.javaClass.canonicalName} occurred while running UseCase: $this\nDetail: ${e.stackTraceToString()}"
            )
            throw e;
        }

    }

}