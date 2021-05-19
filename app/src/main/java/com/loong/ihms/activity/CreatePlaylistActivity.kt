package com.loong.ihms.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.loong.ihms.R
import com.loong.ihms.base.BaseActivity
import com.loong.ihms.databinding.ActivityCreatePlaylistBinding
import com.loong.ihms.model.CuratorAlbum
import com.loong.ihms.model.Song
import com.loong.ihms.utils.ConstantDataUtil
import com.loong.ihms.utils.GeneralUtil.createAlertDialog
import com.loong.ihms.utils.UserRelatedUtil

class CreatePlaylistActivity : BaseActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private lateinit var curatorSongList: ArrayList<Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_playlist)
        curatorSongList = UserRelatedUtil.getCuratorSongList()

        setupToolbar(binding.createPlaylistToolbar)
        binding.createPlaylistToolbar.title = "Playlist Creator"

        setupView()
    }

    private fun setupView() {
        binding.createPlaylistChillLl.setOnClickListener {
            val curatorAlbumList = UserRelatedUtil.getCuratorAlbumList()
            val tempSongList = createChillSongList()
            val number = (curatorAlbumList.size + 1).toString()

            if (tempSongList.size > 0) {
                val tempAlbum = CuratorAlbum(
                    number,
                    "Chill $number",
                    tempSongList,
                    "https://i.imgur.com/RSv02SE.jpeg"
                )

                curatorAlbumList.add(tempAlbum)
                UserRelatedUtil.saveCuratorAlbumList(curatorAlbumList)
                exitAndUpdate()
            } else {
                noSongAvailableAlert()
            }
        }

        binding.createPlaylistUpliftingLl.setOnClickListener {
            val curatorAlbumList = UserRelatedUtil.getCuratorAlbumList()
            val tempSongList = createUpliftingSongList()
            val number = (curatorAlbumList.size + 1).toString()

            if (tempSongList.size > 0) {
                val tempAlbum = CuratorAlbum(
                    number,
                    "Uplifting $number",
                    tempSongList,
                    "https://i.imgur.com/irDsOqj.jpeg"
                )

                curatorAlbumList.add(tempAlbum)
                UserRelatedUtil.saveCuratorAlbumList(curatorAlbumList)
                exitAndUpdate()
            } else {
                noSongAvailableAlert()
            }
        }
    }

    private fun createChillSongList(): ArrayList<Song> {
        val tempList: ArrayList<Song> = ArrayList()

        curatorSongList.forEach { song ->
            if (song.energyPoint < 50
                && song.danceabilityPoint < 50
                && song.valancePoint > 50
                && song.acousticPoint < 50
            ) {
                tempList.add(song)
            }
        }

        return tempList
    }

    private fun createUpliftingSongList(): ArrayList<Song> {
        val tempList: ArrayList<Song> = ArrayList()

        curatorSongList.forEach { song ->
            if (song.energyPoint > 50) {
                tempList.add(song)
            }
        }

        return tempList
    }

    private fun noSongAvailableAlert() {
        createAlertDialog(
            "No songs available",
            "There is no song available to fit into this mood, please try again",
            "OK"
        )
    }

    private fun exitAndUpdate() {
        val localBroadcastIntent = Intent(ConstantDataUtil.UPDATE_PLAYLIST_INTENT)
        LocalBroadcastManager.getInstance(this).sendBroadcast(localBroadcastIntent)

        onBackPressed()
    }
}