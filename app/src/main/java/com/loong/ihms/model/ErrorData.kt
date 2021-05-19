package com.loong.ihms.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorData(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)