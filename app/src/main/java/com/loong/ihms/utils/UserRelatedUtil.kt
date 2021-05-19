package com.loong.ihms.utils

import com.google.gson.Gson
import com.loong.ihms.model.CuratorAlbum
import com.loong.ihms.model.Song

object UserRelatedUtil {
    // Main API Url

    fun getMainApiUrl(): String {
        return LocalStorageUtil.getInstance().readString(LocalStorageUtil.MAIN_API_URL)
    }

    fun saveMainApiUrl(data: String) {
        LocalStorageUtil.getInstance().writeString(LocalStorageUtil.MAIN_API_URL, data)
    }

    // User API Auth

    fun getUserApiAuth(): String {
        return LocalStorageUtil.getInstance().readString(LocalStorageUtil.USER_API_AUTH)
    }

    fun saveUserApiAuth(data: String) {
        LocalStorageUtil.getInstance().writeString(LocalStorageUtil.USER_API_AUTH, data)
    }

    // Local Song List

    fun getAllSongList(): ArrayList<Song> {
        val songListJsonStr = LocalStorageUtil.readString(LocalStorageUtil.USER_SONG_LIST)

        return if (songListJsonStr.isNotEmpty()) {
            Gson().fromJson(songListJsonStr)
        } else {
            ArrayList()
        }
    }

    fun saveAllSongList(songList: ArrayList<Song>) {
        val songListJsonStr = Gson().toJson(songList)
        LocalStorageUtil.writeString(LocalStorageUtil.USER_SONG_LIST, songListJsonStr)
    }

    // Local Curator Song List

    fun getCuratorSongList(): ArrayList<Song> {
        val curatorSongListJsonStr = LocalStorageUtil.readString(LocalStorageUtil.CURATOR_SONG_LIST)

        return if (curatorSongListJsonStr.isNotEmpty()) {
            Gson().fromJson(curatorSongListJsonStr)
        } else {
            ArrayList()
        }
    }

    fun saveCuratorSongList(songList: ArrayList<Song>) {
        val curatorSongListJsonStr = Gson().toJson(songList)
        LocalStorageUtil.writeString(LocalStorageUtil.CURATOR_SONG_LIST, curatorSongListJsonStr)
    }

    // Local Curator Album List

    fun getCuratorAlbumList(): ArrayList<CuratorAlbum> {
        val curatorAlbumListJsonStr = LocalStorageUtil.readString(LocalStorageUtil.CURATOR_ALBUM_LIST)

        return if (curatorAlbumListJsonStr.isNotEmpty()) {
            Gson().fromJson(curatorAlbumListJsonStr)
        } else {
            ArrayList()
        }
    }

    fun saveCuratorAlbumList(curatorAlbumList: ArrayList<CuratorAlbum>) {
        val curatorAlbumListJsonStr = Gson().toJson(curatorAlbumList)
        LocalStorageUtil.writeString(LocalStorageUtil.CURATOR_ALBUM_LIST, curatorAlbumListJsonStr)
    }
}