package org.appcenter.inudorm.networking

import okhttp3.OkHttpClient
import org.appcenter.inudorm.networking.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(AuthInterceptor()) // 저장한 토큰을 헤더에 넣는 Interceptor
        .addNetworkInterceptor(ResponseInterceptor()) // Response Wrapper를 벗겨내는 Interceptor
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val service : Service by lazy {
        retrofit.create(Service::class.java)
    }
}