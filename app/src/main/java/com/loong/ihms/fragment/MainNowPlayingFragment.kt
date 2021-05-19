package com.loong.ihms.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.loong.ihms.R
import com.loong.ihms.databinding.FragmentMainNowPlayingBinding
import com.loong.ihms.model.Song
import com.loong.ihms.utils.getBaseActivity

class MainNowPlayingFragment : Fragment(R.layout.fragment_main_now_playing) {
    private lateinit var binding: FragmentMainNowPlayingBinding
    private var exoPlayer: ExoPlayer? = null
    private var mainSongList: ArrayList<Song> = ArrayList()

    private val playbackStateListener: Player.EventListener = object : Player.EventListener {
        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            super.onTracksChanged(trackGroups, trackSelections)
            val position = exoPlayer?.currentWindowIndex ?: -1

            if (mainSongList.size > 0 && position >= 0) {
                val song = mainSongList[position]

                binding.playSongTitleTv.text = song.title
                binding.playSongAlbumTv.text = song.album.name
                binding.playSongArtistTv.text = song.artist.name
                binding.playSongStreamingTv.text = "Streaming on: ${song.getMediaType()}"

                Glide.with(getBaseActivity())
                    .load(song.art)
                    .into(binding.playSongImg)
            }
        }

        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                ExoPlayer.STATE_IDLE -> {

                }
                ExoPlayer.STATE_BUFFERING -> {

                }
                ExoPlayer.STATE_READY -> {

                }
                ExoPlayer.STATE_ENDED -> {

                }
                else -> {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainNowPlayingBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = SimpleExoPlayer.Builder(getBaseActivity()).build()
            binding.playSongPv.player = exoPlayer

            exoPlayer?.addListener(playbackStateListener)
            exoPlayer?.prepare()
        }
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer?.removeListener(playbackStateListener)
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    fun startPlayingSong(songList: ArrayList<Song>, position: Int) {
        binding.playSongTitleTv.text = ""
        binding.playSongAlbumTv.text = ""
        binding.playSongArtistTv.text = ""
        binding.playSongStreamingTv.text = ""

        exoPlayer?.clearMediaItems()
        mainSongList = songList

        mainSongList.forEach { song ->
            val mediaItem: MediaItem = MediaItem.fromUri(song.url)
            exoPlayer?.addMediaItem(mediaItem)
        }

        exoPlayer?.seekTo(position, 0)
        exoPlayer?.playWhenReady = true
    }
}