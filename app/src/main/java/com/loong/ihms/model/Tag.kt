package com.loong.ihms.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Tag(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)