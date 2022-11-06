package org.appcenter.inudorm.util

import android.util.Log

class IDormLogger {
    companion object {
        private const val prefix = "["
        @JvmStatic fun d(currentScope: Any, message: String) {
            Log.d("$prefix${currentScope::class.simpleName}]", message)
        }
        @JvmStatic fun i(currentScope: Any, message: String) {
            Log.i("$prefix${currentScope::class.simpleName}]", message)
        }
        @JvmStatic fun v(currentScope: Any, message: String) {
            Log.v("$prefix${currentScope::class.simpleName}]", message)
        }
    }
}
