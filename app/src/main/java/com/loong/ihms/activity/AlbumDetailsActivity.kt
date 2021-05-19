package com.loong.ihms.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.loong.ihms.R
import com.loong.ihms.adapter.RecyclerViewBaseAdapter
import com.loong.ihms.base.BaseActivity
import com.loong.ihms.databinding.ActivityAlbumDetailsBinding
import com.loong.ihms.databinding.ViewTrackItemBinding
import com.loong.ihms.model.Album
import com.loong.ihms.model.CuratorAlbum
import com.loong.ihms.model.Song
import com.loong.ihms.network.ApiRepositoryFunction
import com.loong.ihms.network.ApiResponseCallback
import com.loong.ihms.utils.*

class AlbumDetailsActivity : BaseActivity() {                 //used to show album details and playlist detail
    private lateinit var binding: ActivityAlbumDetailsBinding

    private lateinit var adapter: SongListAdapter
    private var albumId: Int = 0
    private var albumItem: Album? = null                    //remote server album detail
    private var albumPlaylistItem: CuratorAlbum? = null     //local storage album detail (record data, not real object)
    private var songList: ArrayList<Song> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_details)
        albumId = intent.getIntExtra(ConstantDataUtil.ALBUM_DETAILS_ID_PARAMS, 0)

        setupToolbar(binding.albumDetailsToolbar)

        binding.albumDetailsRv.layoutManager = LinearLayoutManager(this)
        binding.albumDetailsRv.addItemDecoration(SpaceItemDecoration(8.dp))

        if (albumId == 0) {
            albumId = intent.getIntExtra(ConstantDataUtil.ALBUM_PLAYLIST_DETAILS_ID_PARAMS, 0)
            getAlbumPlaylistData()
        } else {
            getAlbumData()
        }
    }

    // From local

    private fun getAlbumPlaylistData() {     //get playlist detail
        val curatorAlbum = UserRelatedUtil.getCuratorAlbumList()
        albumPlaylistItem = curatorAlbum.find { it.id == albumId.toString() }

        albumPlaylistItem?.let {
            songList = it.songList
        }

        setupView()
    }

    // From backend

    private fun getAlbumData() {
        showHideLoadingDialog(true)

        ApiRepositoryFunction.getAlbumDetails(albumId, object : ApiResponseCallback<Album> {
            override fun onSuccess(responseData: Album) {
                albumItem = responseData
                getAlbumSongList()
            }

            override fun onFailed() {
                albumItem = null
                getAlbumSongList()
            }
        })
    }

    private fun getAlbumSongList() {
        ApiRepositoryFunction.getAlbumSongList(albumId, object : ApiResponseCallback<ArrayList<Song>> {
            override fun onSuccess(responseData: ArrayList<Song>) {
                showHideLoadingDialog(false)

                songList = responseData
                setupView()
            }

            override fun onFailed() {
                showHideLoadingDialog(false)
            }
        })
    }

    // Others

    private fun setupView() {
        if (albumItem != null) {
            binding.albumDetailsToolbar.title = albumItem?.name ?: ""

            Glide.with(this)
                .load(albumItem?.art ?: "")
                .into(binding.albumDetailsArtImg)
        } else if (albumPlaylistItem != null) {
            binding.albumDetailsToolbar.title = albumPlaylistItem?.name ?: ""

            Glide.with(this)
                .load(albumPlaylistItem?.art ?: "")
                .into(binding.albumDetailsArtImg)
        }

        adapter = SongListAdapter(this, songList)
        binding.albumDetailsRv.adapter = adapter

        binding.albumDetailsPlayFab.setOnClickListener {
            startPlaying(0)
        }
    }

    private fun startPlaying(position: Int) {
        val songListJsonStr = Gson().toJson(songList)

        val localBroadcastIntent = Intent(ConstantDataUtil.START_PLAYING_INTENT)
        localBroadcastIntent.putExtra(ConstantDataUtil.START_PLAYING_SONG_LIST_EXTRA, songListJsonStr)
        localBroadcastIntent.putExtra(ConstantDataUtil.START_PLAYING_SONG_POSITION_EXTRA, position)
        LocalBroadcastManager.getInstance(this).sendBroadcast(localBroadcastIntent)

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private class SongListAdapter(val activity: AlbumDetailsActivity, val itemList: ArrayList<Song>) : RecyclerViewBaseAdapter() {
        override fun setBindViewHolder(viewHolder: MyViewHolder, position: Int) {
            val binding: ViewTrackItemBinding = viewHolder.binding as ViewTrackItemBinding
            val itemData: Song = itemList[position]

            binding.trackTitleTv.text = itemData.title
            binding.trackDurationTv.text = GeneralUtil.fromSecToMinSec(itemData.time)
            binding.trackArtistTv.text = itemData.artist.name

            if (position % 2 == 0) {
                binding.trackMainLl.background = ContextCompat.getDrawable(activity, R.drawable.bg_item_gray_corner)
            } else {
                binding.trackMainLl.background = ContextCompat.getDrawable(activity, R.drawable.bg_item_white_corner)
            }

            binding.trackPlayImg.setOnClickListener {
                activity.startPlaying(position)
            }
        }

        override fun getLayoutIdForPosition(position: Int): Int {
            return R.layout.view_track_item
        }

        override fun getItemTotalCount(): Int {
            return itemList.size
        }
    }
}