package org.appcenter.inudorm.networking

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("responseCode") var responseCode: String,
    @SerializedName("data") var data: T? = null,
    @SerializedName("responseMessage") var responseMessage: String? = null
)