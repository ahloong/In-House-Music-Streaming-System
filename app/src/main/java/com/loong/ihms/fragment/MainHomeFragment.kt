package com.loong.ihms.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.loong.ihms.R
import com.loong.ihms.adapter.CustomFragmentPagerAdapter
import com.loong.ihms.databinding.FragmentMainHomeBinding
import java.util.*

class MainHomeFragment : Fragment(R.layout.fragment_main_home) {
    private lateinit var binding: FragmentMainHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainHomeBinding.bind(view)

        setupViewPager()
    }

    private fun setupViewPager() {              //tab view in main interface
        val fragmentList = ArrayList<Fragment>()
        val fragmentTitleList = ArrayList<String>()

        fragmentList.add(HomeAlbumFragment())
        fragmentList.add(HomePlaylistFragment())
        fragmentList.add(HomeArtistFragment())

        fragmentTitleList.add("Album")
        fragmentTitleList.add("Playlist")
        fragmentTitleList.add("Artist")

        val customFragmentPagerAdapter = CustomFragmentPagerAdapter(childFragmentManager, fragmentList, fragmentTitleList)
        binding.homeVp.adapter = customFragmentPagerAdapter
        binding.homeVp.offscreenPageLimit = 3
        binding.homeTl.setupWithViewPager(binding.homeVp)
    }
}