package com.loong.ihms.utils

object ConstantDataUtil {
    // Curator Category
    const val DEFAULT_CATEGORY_VALUE = 50

    // API Version
    const val API_VERSION = "350001"

    // API Action Type
    const val ACTION_HANDSHAKE = "handshake"
    const val ACTION_SONGS = "songs"
    const val ACTION_ALBUMS = "albums"
    const val ACTION_ALBUM = "album"
    const val ACTION_ALBUM_SONGS = "album_songs"
    const val ACTION_ARTISTS = "artists"
    const val ACTION_ARTIST_ALBUMS = "artist_albums"

    // API Type
    const val TYPE_SONG = "song"
    const val TYPE_ALBUM = "album"

    // Extra Params Name
    const val IP_LOGIN_PARAMS = "ip_login_params"
    const val ALBUM_DETAILS_ID_PARAMS = "album_details_id_params"
    const val ALBUM_PLAYLIST_DETAILS_ID_PARAMS = "album_playlist_details_id_params"
    const val ARTIST_ALBUM_ID_PARAMS = "artist_album_id_params"
    const val ARTIST_ALBUM_NAME_PARAMS = "artist_album_name_params"

    // Local Broadcast
    const val UPDATE_PLAYLIST_INTENT = "update_playlist_intent"
    const val START_PLAYING_INTENT = "start_playing_intent"
    const val START_PLAYING_SONG_LIST_EXTRA = "start_playing_song_list_extra"
    const val START_PLAYING_SONG_POSITION_EXTRA = "start_playing_song_position_extra"
}