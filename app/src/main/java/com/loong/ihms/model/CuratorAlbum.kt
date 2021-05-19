package com.loong.ihms.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CuratorAlbum(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("songlist") val songList: ArrayList<Song>,
    @SerializedName("art") val art: String
) {
    fun getArtistNames(): String {
        var names = ""

        songList.forEachIndexed { index, item ->
            if (index > 3) {
                names = "${names}, ..."
                return@forEachIndexed
            } else {
                names = if (names.isEmpty()) {
                    item.artist.name
                } else {
                    "${names}, ${item.artist.name}"
                }
            }
        }

        return names
    }
}