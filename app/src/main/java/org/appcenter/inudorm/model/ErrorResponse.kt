package org.appcenter.inudorm.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("responseCode")
    val code: String,
    val error: String,
    @SerializedName("responseMessage")
    val message: String)