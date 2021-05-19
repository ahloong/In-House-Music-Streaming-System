package com.loong.ihms.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("year") val year: Int,
    @SerializedName("tracks") val tracks: Int,
    @SerializedName("disk") val disk: Int,
    @SerializedName("tag") val tag: List<Tag>,
    @SerializedName("art") val art: String,
    @SerializedName("flag") val flag: Int,
    @SerializedName("preciserating") val preciserating: Any,
    @SerializedName("rating") val rating: Any,
    @SerializedName("averagerating") val averagerating: Any,
    @SerializedName("mbid") val mbid: Any
)