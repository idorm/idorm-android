package org.appcenter.inudorm.networking

import okhttp3.Interceptor
import okhttp3.Response
import org.appcenter.inudorm.App

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req =
            chain.request().newBuilder().addHeader("Authorization", App.prefs.savedUser?.token ?: "").build()
        return chain.proceed(req)
    }
}