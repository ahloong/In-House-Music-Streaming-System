package com.loong.ihms.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("albums") val albums: Int,
    @SerializedName("songs") val songs: Int,
    @SerializedName("tag") val tag: List<Tag>,
    @SerializedName("art") val art: String,
    @SerializedName("flag") val flag: Int,
    @SerializedName("rating") val rating: Any
)