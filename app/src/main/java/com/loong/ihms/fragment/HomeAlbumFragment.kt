package com.loong.ihms.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.loong.ihms.R
import com.loong.ihms.adapter.AlbumListAdapter
import com.loong.ihms.databinding.FragmentHomeAlbumBinding
import com.loong.ihms.model.Album
import com.loong.ihms.network.ApiRepositoryFunction
import com.loong.ihms.network.ApiResponseCallback
import com.loong.ihms.utils.SpaceItemDecoration
import com.loong.ihms.utils.dp
import com.loong.ihms.utils.getBaseActivity

class HomeAlbumFragment : Fragment(R.layout.fragment_home_album) {
    private lateinit var binding: FragmentHomeAlbumBinding
    private lateinit var adapter: AlbumListAdapter
    private var dataItemList: ArrayList<Album> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeAlbumBinding.bind(view)

        binding.albumContentRv.layoutManager = GridLayoutManager(getBaseActivity(), 2)
        binding.albumContentRv.addItemDecoration(SpaceItemDecoration(16.dp))

        binding.albumContentSrl.setOnRefreshListener {
            getDataList()
        }

        getDataList()
    }

    private fun getDataList() {                   //get album list from remote server's json
        binding.albumContentSrl.isRefreshing = true

        ApiRepositoryFunction.getAlbumList(object : ApiResponseCallback<ArrayList<Album>> {
            override fun onSuccess(responseData: ArrayList<Album>) {
                binding.albumContentSrl.isRefreshing = false
                dataItemList = responseData
                setupList()
            }

            override fun onFailed() {
                binding.albumContentSrl.isRefreshing = false
                dataItemList = ArrayList()
                setupList()
            }
        })
    }

    private fun setupList() {        //for recycler view
        adapter = AlbumListAdapter(getBaseActivity(), dataItemList)
        binding.albumContentRv.adapter = adapter
    }
}