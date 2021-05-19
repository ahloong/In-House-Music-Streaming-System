package com.loong.ihms.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object LocalStorageUtil {
    var MAIN_API_URL = "main_api_url"
    var USER_API_AUTH = "user_api_auth"
    var USER_SONG_LIST = "user_song_list"
    var CURATOR_SONG_LIST = "curator_song_list"
    var CURATOR_ALBUM_LIST = "curator_album_list"

    var DEFAULT_STRING = ""
    var DEFAULT_INT = -1
    var DEFAULT_BOOLEAN = false

    private var selfInstance: LocalStorageUtil? = null
    private var sharedPreferences: SharedPreferences? = null

    fun getInstance(): LocalStorageUtil {
        if (selfInstance == null) {
            selfInstance = LocalStorageUtil
        }

        return selfInstance!!
    }

    fun initialize(context: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun writeString(key: String, value: String) {
        sharedPreferences!!.edit().putString(key, value).apply()
    }

    fun readString(key: String): String {
        return sharedPreferences!!.getString(key, DEFAULT_STRING)!!
    }

    fun writeInt(key: String, value: Int) {
        sharedPreferences!!.edit().putInt(key, value).apply()
    }

    fun readInt(key: String): Int {
        return sharedPreferences!!.getInt(key, DEFAULT_INT)
    }

    fun writeBoolean(key: String, value: Boolean) {
        sharedPreferences!!.edit().putBoolean(key, value).apply()
    }

    fun readBoolean(key: String): Boolean {
        return sharedPreferences!!.getBoolean(key, DEFAULT_BOOLEAN)
    }
}