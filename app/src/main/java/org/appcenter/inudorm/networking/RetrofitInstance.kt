package org.appcenter.inudorm.networking

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import org.appcenter.inudorm.App
import org.appcenter.inudorm.networking.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor()) // 저장한 토큰을 헤더에 넣는 Interceptor
        .addInterceptor(ResponseInterceptor()) // Response Wrapper를 벗겨내는 Interceptor
        .build()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(App.gson))
            .client(client)
            .build()
    }

    val service: Service by lazy {
        retrofit.create(Service::class.java)
    }
}