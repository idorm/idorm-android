package org.appcenter.inudorm.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("responseCode")
    val code: String,
    val error: String,
    val status: Int? = -999,
    @SerializedName("responseMessage")
    val message: String,
)