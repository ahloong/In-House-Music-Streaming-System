package com.loong.ihms.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.loong.ihms.R
import com.loong.ihms.adapter.AlbumListAdapter
import com.loong.ihms.base.BaseActivity
import com.loong.ihms.databinding.ActivityArtistAlbumListBinding
import com.loong.ihms.model.Album
import com.loong.ihms.network.ApiRepositoryFunction
import com.loong.ihms.network.ApiResponseCallback
import com.loong.ihms.utils.ConstantDataUtil
import com.loong.ihms.utils.SpaceItemDecoration
import com.loong.ihms.utils.dp

class ArtistAlbumListActivity : BaseActivity() {
    private lateinit var binding: ActivityArtistAlbumListBinding
    private lateinit var adapter: AlbumListAdapter

    private var artistId: Int = 0
    private var artistName: String = ""
    private var dataItemList: ArrayList<Album> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_album_list)
        artistId = intent.getIntExtra(ConstantDataUtil.ARTIST_ALBUM_ID_PARAMS, 0)
        artistName = intent.getStringExtra(ConstantDataUtil.ARTIST_ALBUM_NAME_PARAMS) ?: ""

        setupToolbar(binding.artistAlbumToolbar)

        binding.artistAlbumRv.layoutManager = GridLayoutManager(this, 2)
        binding.artistAlbumRv.addItemDecoration(SpaceItemDecoration(16.dp))

        if (artistId > 0) {
            getArtistAlbumList()
        }
    }

    private fun getArtistAlbumList() {
        ApiRepositoryFunction.getArtistAlbumList(artistId, object : ApiResponseCallback<ArrayList<Album>> {
            override fun onSuccess(responseData: ArrayList<Album>) {
                dataItemList = responseData
                setupView()
            }

            override fun onFailed() {
                dataItemList = ArrayList()
                setupView()
            }
        })
    }

    private fun setupView() {
        binding.artistAlbumToolbar.title = artistName

        adapter = AlbumListAdapter(this, dataItemList)
        binding.artistAlbumRv.adapter = adapter
    }
}