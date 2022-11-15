package org.appcenter.inudorm.networking

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.appcenter.inudorm.App
import org.appcenter.inudorm.util.IDormLogger
import java.nio.Buffer

class AuthInterceptor : Interceptor {
    private val TAG = "[AuthInterceptor]"
    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request()
        val buffer = okio.Buffer()
        copy.body()?.writeTo(buffer);
        Log.i(
            TAG,
            "Send ${chain.request().method()} request to ${
                chain.request().url()
            } with\ntoken: ${App.token},\nbody: ${buffer.readUtf8()},\n"
        )
        var req =
            chain.request().newBuilder()
                .addHeader("X-AUTH-TOKEN", App.token ?: "").build()
        return chain.proceed(req)

    }
}


class ResponseInterceptor : Interceptor {
    private val TAG = "[ResponseInterceptor]"
    private val networkErrorMessage = "네트워크 요청에 알 수 없는 이유로 실패했습니다."
    override fun intercept(chain: Interceptor.Chain): Response {
        val gson = Gson()
        val request = chain.request()
        // Request
        val response = chain.proceed(request)

        // Get raw json response
        val rawJsonResponse: String? = response.body()?.string()
        IDormLogger.i(
            this,
            "${
                request.url().encodedPath()
            } response:\n${rawJsonResponse}\n-----------------------\n"
        )

        // Convert json to data object
        val type = object : TypeToken<ResponseWrapper<*>>() {}.type
        val res = try {
            val result =
                gson.fromJson<ResponseWrapper<*>>(rawJsonResponse, type) // parsed ResponseWrapper
                    ?: throw JsonParseException("Failed to parse json")
            IDormLogger.i(this, result.toString())
            if (response.isSuccessful) {
                Data(data = result.data ?: true)
            } else {
                Data(error = result.responseMessage)
            }
        } catch (e: JsonSyntaxException) {
            IDormLogger.i(this, "JSON 문법 오류. 바디가 문자열 같습니다.")
            Data(data = true)

        } catch (e: Throwable) {
            IDormLogger.i(this, e.toString())
            Data<Nothing>(error = networkErrorMessage)
        }

        // Re-transform result to json and return
        val resultJson = gson.toJson(res)
        val newResponse = response.newBuilder()
            .body(ResponseBody.create(MediaType.parse("application/json"), resultJson))
            .build()
        Log.d(TAG, "${res.data} | $newResponse")
        return newResponse
    }
}