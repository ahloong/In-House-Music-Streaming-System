package com.loong.ihms.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.loong.ihms.R
import com.loong.ihms.activity.CreatePlaylistActivity
import com.loong.ihms.adapter.AlbumPlaylistListAdapter
import com.loong.ihms.databinding.FragmentHomePlaylistBinding
import com.loong.ihms.model.CuratorAlbum
import com.loong.ihms.utils.*

class HomePlaylistFragment : Fragment(R.layout.fragment_home_playlist) {
    private lateinit var binding: FragmentHomePlaylistBinding
    private lateinit var adapter: AlbumPlaylistListAdapter
    private var dataItemList: ArrayList<CuratorAlbum> = ArrayList()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Handler(Looper.getMainLooper()).postDelayed({
                getDataList()
            }, 500)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomePlaylistBinding.bind(view)

        binding.playlistContentRv.layoutManager = GridLayoutManager(getBaseActivity(), 2)
        binding.playlistContentRv.addItemDecoration(SpaceItemDecoration(16.dp))

        binding.playlistContentSrl.setOnRefreshListener {
            getDataList()
        }

        binding.playlistCreateMb.setOnClickListener {
            val intent = Intent(getBaseActivity(), CreatePlaylistActivity::class.java)
            getBaseActivity().startActivity(intent)
        }

        getDataList()

        LocalBroadcastManager
            .getInstance(getBaseActivity())
            .registerReceiver(
                broadcastReceiver,
                IntentFilter(ConstantDataUtil.UPDATE_PLAYLIST_INTENT)
            )
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(getBaseActivity()).unregisterReceiver(broadcastReceiver);
        super.onDestroy()
    }

    private fun getDataList() {
        binding.playlistContentSrl.isRefreshing = false

        dataItemList = UserRelatedUtil.getCuratorAlbumList()
        setupList()
    }

    private fun setupList() {
        adapter = AlbumPlaylistListAdapter(getBaseActivity(), dataItemList)
        binding.playlistContentRv.adapter = adapter
    }
}