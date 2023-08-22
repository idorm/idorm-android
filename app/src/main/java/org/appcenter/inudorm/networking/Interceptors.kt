package org.appcenter.inudorm.networking

import android.util.Log
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.ErrorResponse
import org.appcenter.inudorm.util.IDormLogger
import java.io.IOException
import java.util.concurrent.TimeUnit

class AuthInterceptor : Interceptor {
    private val TAG = "[AuthInterceptor]"
    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request()
        val buffer = okio.Buffer()
        copy.body?.writeTo(buffer);
        Log.i(
            TAG,
            "Send ${chain.request().method} request to ${
                chain.request().url
            } with\ntoken: ${App.token},\nbody: ${buffer.readUtf8()},\n"
        )
        val req =
            chain.request().newBuilder()
                .addHeader("X-AUTH-TOKEN", App.token ?: "").build()
        return chain.proceed(req)

    }
}


class ResponseInterceptor : Interceptor {
    inline fun <reified T : Enum<T>> String.asEnumOrDefault(defaultValue: T? = null): T? =
        enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) } ?: defaultValue

    private val TAG = "[ResponseInterceptor]"
    private val networkErrorMessage = "네트워크 요청에 알 수 없는 이유로 실패했습니다."
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // Request
        val response = chain.proceed(request)

        // Get raw json response
        val rawJsonResponse: String? = response.body?.string()
        IDormLogger.i(
            this,
            "${request.method} | ${
                request.url.encodedPath
            } response:\n${rawJsonResponse}\n-----------------------\n"
        )

        // Convert json to data object
        val res = try {
            // 성공한 경우
            if (response.isSuccessful) {
                if (rawJsonResponse.isNullOrEmpty()) { // No Content => 빈 배열 반환 []
                    emptyList<Any>()
                } else {
                    val type = object : TypeToken<ResponseWrapper<*>>() {}.type
                    val result =
                        App.gson.fromJson<ResponseWrapper<*>>(
                            rawJsonResponse,
                            type
                        ) // parsed ResponseWrapper
                            ?: throw JsonParseException("Failed to parse json")
                    IDormLogger.i(this, result.toString())
                    result.data
                }
            } else {
                // 정상적으로 실패한 경우
                val type = object : TypeToken<ErrorResponse>() {}.type
                val result =
                    App.gson.fromJson<ErrorResponse>(
                        rawJsonResponse,
                        type
                    ) // parsed ResponseWrapper
                        ?: throw IOException("${response.code} 에러 발생.")

                val emptyList = App.gson.toJson(listOf<Any>())
                val newResponse = response.newBuilder()
                    .code(200)
                    .body(emptyList.toResponseBody("application/json".toMediaTypeOrNull()))
                    .build()

                if (result.status != null) return newResponse
                IDormLogger.e(
                    this,
                    " ${result.code} |  ${result.code.asEnumOrDefault<ErrorCode>(null)}"
                )
                throw IDormError(
                    (result.code.asEnumOrDefault<ErrorCode>(null) ?: ErrorCode.UNKNOWN_ERROR)
                )
            }
        } catch (e: JsonSyntaxException) {
            // JSON 문법 오류인 경우
            IDormLogger.i(this, "JSON 문법 오류. 바디가 문자열 같습니다.")
            throw IOException("Failed to parse json")
        } catch (e: IDormError) {
            throw e
        } catch (e: Throwable) {
            IDormLogger.e(this, e.toString())
            throw IOException(e.message)
        }

        // Re-transform result to json and return
        val resultJson = App.gson.toJson(res)
        IDormLogger.i(this, resultJson)
        val newResponse = response.newBuilder()
            .body(ResponseBody.create("application/json".toMediaTypeOrNull(), resultJson))
            .code(200)
            .build()
        Log.d(TAG, "$res | $newResponse")
        return newResponse
    }
}