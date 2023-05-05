package org.appcenter.inudorm.usecase

import android.util.Log
import io.sentry.Sentry
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.util.IDormLogger

abstract class UseCase<ParamT, ResultT>() {
    private val TAG = "[UseCase]"

    suspend fun run(params: ParamT): ResultT {
        try {
            Log.d(TAG, "Running UseCase $this with following params: $params")
            return onExecute(params)
        } catch (e: Exception) {
            Sentry.captureException(e)
            IDormLogger.e(
                this,
                "Exception ${e.javaClass.canonicalName} occurred while running UseCase: $this\nDetail: ${e.stackTraceToString()}"
            )
            throw e;
        }

    }

    abstract suspend fun onExecute(params: ParamT): ResultT
}