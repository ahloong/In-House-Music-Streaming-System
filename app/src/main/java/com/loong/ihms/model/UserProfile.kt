package com.loong.ihms.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserProfile(
    @SerializedName("auth") val auth: String,
    @SerializedName("api") val api: String,
    @SerializedName("session_expire") val sessionExpire: String,
    @SerializedName("update") val update: String,
    @SerializedName("add") val add: String,
    @SerializedName("clean") val clean: String,
    @SerializedName("songs") val songs: Int,
    @SerializedName("albums") val albums: Int,
    @SerializedName("artists") val artists: Int,
    @SerializedName("playlists") val playlists: Int,
    @SerializedName("videos") val videos: Int,
    @SerializedName("catalogs") val catalogs: Int,
    @SerializedName("error") val error: ErrorData?
)