package org.appcenter.inudorm.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow

abstract class UseCase<ParamT, ResultT> () {
    private val TAG = "[UseCase]"

    suspend fun run(params: ParamT?):Flow<ResultT> {
        Log.d(TAG, "Running UseCase $this with following params: $params")
        return onExecute(params)
    }

    abstract suspend fun onExecute(params:ParamT?): Flow<ResultT>
}