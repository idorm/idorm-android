package org.appcenter.inudorm.networking

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.appcenter.inudorm.App

class AuthInterceptor : Interceptor {
    private val TAG = "[AuthInterceptor]"
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "Send request to ${chain.request().url()} with token: ${App.prefs.savedUser?.token}")
        var req =
            chain.request().newBuilder()
                .addHeader("Authorization", App.prefs.savedUser?.token ?: "").build()
        return chain.proceed(req)

    }
}

class ResponseInterceptor : Interceptor {
    private val TAG = "[ResponseInterceptor]"
    private val networkErrorMessage = "네트워크 요청에 실패했습니다."
    override fun intercept(chain: Interceptor.Chain): Response {
        val gson = Gson()
        val request = chain.request()
        // Request
        val response = chain.proceed(request)
        Log.d(TAG, response.toString())
        // Get raw json response
        val rawJsonResponse: String = response.body().toString()

        // Convert json to data object
        val type = object : TypeToken<ResponseWrapper<*>>() {}.type
        val res = try {
            val result = gson.fromJson<ResponseWrapper<*>>(rawJsonResponse, type) // parsed ResponseWrapper
                ?: throw JsonParseException("Failed to parse json")
            ResponseWrapper(result.responseCode, result.data)
        } catch (e: JsonParseException) {
            ResponseWrapper(NetworkErrorCode.FAILED_TO_PARSE_JSON, null, networkErrorMessage)
        } catch (t: Throwable) {
            ResponseWrapper(NetworkErrorCode.UNKNOWN_ERROR, null, networkErrorMessage)
        }


        // Re-transform result to json and return
        val resultJson = gson.toJson(res.data)
        val newResponse = response.newBuilder()
            .message(res.responseMessage ?: response.message())
            .body(ResponseBody.create(MediaType.parse("application/json"), resultJson))
            .build()
        Log.d(TAG, "${res.responseCode} | $newResponse")
        return newResponse
    }
}