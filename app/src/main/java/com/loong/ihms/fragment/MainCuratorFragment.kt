package com.loong.ihms.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.loong.ihms.R
import com.loong.ihms.databinding.FragmentMainCuratorBinding
import com.loong.ihms.model.Song
import com.loong.ihms.utils.ConstantDataUtil
import com.loong.ihms.utils.UserRelatedUtil
import com.loong.ihms.utils.getBaseActivity

class MainCuratorFragment : Fragment(R.layout.fragment_main_curator) {
    companion object {
        const val CATEGORY_ENERGY = 0
        const val CATEGORY_DANCEABILITY = 1
        const val CATEGORY_VALANCE = 2
        const val CATEGORY_ACOUSTIC = 3
    }

    private lateinit var binding: FragmentMainCuratorBinding

    private var allSongList: ArrayList<Song> = ArrayList()
    private var curatorSongList: ArrayList<Song> = ArrayList()

    private val categoryList: ArrayList<CuratorCategory> by lazy {
        arrayListOf(
            CuratorCategory("Energy", "Do you feel energetic when listening to this song?", "Less Energetic", "More Energetic"),
            CuratorCategory("Danceability", "Do you feel you would like to dance to this song?", "No", "Yes"),
            CuratorCategory("Valance", "Do you feel this song give you positive mood?", "Less Positive", "More Positive"),
            CuratorCategory("Acoustic", "How acoustic the music is?", "Less Acoustic", "More Acoustic")
        )
    }

    private var currentSongItem: Song? = null
    private var currentSongPosition: Int = 0
    private var currentCategoryPosition: Int = CATEGORY_ENERGY

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainCuratorBinding.bind(view)

        allSongList = UserRelatedUtil.getAllSongList()
        curatorSongList = UserRelatedUtil.getCuratorSongList()

        if (allSongList.size > 0) {
            binding.curatorMainLl.visibility = View.VISIBLE

            if (curatorSongList.size > 0) {
                val tempPosition = allSongList.indexOfFirst { it.id == curatorSongList.last().id }
                currentSongPosition = if (tempPosition >= 0) (tempPosition + 1) else 0
            }

            setupMainView()
            setupSongView()
            setupCuratorView()
        } else {
            binding.curatorMainLl.visibility = View.INVISIBLE
        }
    }

    private fun setupMainView() {
        // Song

        binding.curatorSongPreviousFab.setOnClickListener {
            if (currentSongPosition > 0) {
                currentSongPosition -= 1
                currentCategoryPosition = 0

                setupSongView()
                setupCuratorView()
            }
        }

        binding.curatorSongNextFab.setOnClickListener {
            if (currentSongPosition < (allSongList.size - 1)) {
                currentSongPosition += 1
                currentCategoryPosition = 0

                setupSongView()
                setupCuratorView()
            }
        }

        binding.curatorSongPlayFab.setOnClickListener {
            val currentItem = allSongList[currentSongPosition]
            val songList = arrayListOf(currentItem)
            val songListJsonStr = Gson().toJson(songList)

            val localBroadcastIntent = Intent(ConstantDataUtil.START_PLAYING_INTENT)
            localBroadcastIntent.putExtra(ConstantDataUtil.START_PLAYING_SONG_LIST_EXTRA, songListJsonStr)
            localBroadcastIntent.putExtra(ConstantDataUtil.START_PLAYING_SONG_POSITION_EXTRA, 0)
            LocalBroadcastManager.getInstance(getBaseActivity()).sendBroadcast(localBroadcastIntent)
        }

        // Curator category

        binding.curatorSongCatPreviousMb.setOnClickListener {
            if (currentCategoryPosition > CATEGORY_ENERGY) {
                currentCategoryPosition -= 1
                setupCuratorView()
            }
        }

        binding.curatorSongCatNextMb.setOnClickListener {
            if (currentCategoryPosition < CATEGORY_ACOUSTIC) {
                currentCategoryPosition += 1
                setupCuratorView()
            }
        }

        binding.curatorSongCatDoneMb.setOnClickListener {
            currentSongItem?.isCurated = true

            currentSongItem?.let { currentSong ->                                   //check whether song has been curated before
                val curatedSongPosition = curatorSongList.indexOfFirst { curated ->
                    curated.id == currentSong.id
                }

                if (curatedSongPosition >= 0) {
                    curatorSongList[curatedSongPosition].energyPoint = currentSong.energyPoint
                    curatorSongList[curatedSongPosition].danceabilityPoint = currentSong.danceabilityPoint
                    curatorSongList[curatedSongPosition].valancePoint = currentSong.valancePoint
                    curatorSongList[curatedSongPosition].acousticPoint = currentSong.acousticPoint
                } else {
                    curatorSongList.add(currentSong)
                }

                UserRelatedUtil.saveCuratorSongList(curatorSongList)
            }

            binding.curatorSongNextFab.callOnClick()
        }

        binding.curatorCategorySlider.addOnChangeListener { _, value, _ ->
            currentSongItem?.let {
                val tempValue = value.toInt()

                when (currentCategoryPosition) {
                    CATEGORY_ENERGY -> it.energyPoint = tempValue
                    CATEGORY_DANCEABILITY -> it.danceabilityPoint = tempValue
                    CATEGORY_VALANCE -> it.valancePoint = tempValue
                    CATEGORY_ACOUSTIC -> it.acousticPoint = tempValue
                }
            }
        }
    }

    private fun setupSongView() {                                 //get track data to be curated
        currentSongItem = allSongList[currentSongPosition]

        currentSongItem?.let { currentItem ->
            Glide.with(getBaseActivity())
                .load(currentItem.art)
                .into(binding.curatorSongImg)

            binding.curatorSongTitleTv.text = currentItem.name
            binding.curatorSongAlbumTv.text = currentItem.album.name
            binding.curatorSongArtistTv.text = currentItem.artist.name
        }

        checkSongButtons()
    }

    private fun setupCuratorView() {
        val currentCategoryItem = categoryList[currentCategoryPosition]

        binding.curatorCategoryTitleTv.text = currentCategoryItem.name
        binding.curatorCategoryDescTv.text = currentCategoryItem.desc
        binding.curatorCategoryLeftTv.text = currentCategoryItem.leftText
        binding.curatorCategoryRightTv.text = currentCategoryItem.rightText

        currentSongItem?.let { currentItem ->
            val point: Int = when (currentCategoryPosition) {
                CATEGORY_ENERGY -> currentItem.energyPoint
                CATEGORY_DANCEABILITY -> currentItem.danceabilityPoint
                CATEGORY_VALANCE -> currentItem.valancePoint
                CATEGORY_ACOUSTIC -> currentItem.acousticPoint
                else -> ConstantDataUtil.DEFAULT_CATEGORY_VALUE
            }

            binding.curatorCategorySlider.value = point.toFloat()
        }

        checkCategoryButtons()
    }

    private fun checkSongButtons() {             //check whether to be curated song list is at the top or bottom
        when (currentSongPosition) {
            0 -> {
                binding.curatorSongPreviousFab.isEnabled = false
                binding.curatorSongNextFab.isEnabled = true
            }
            (allSongList.size - 1) -> {
                binding.curatorSongPreviousFab.isEnabled = true
                binding.curatorSongNextFab.isEnabled = false
            }
            else -> {
                binding.curatorSongPreviousFab.isEnabled = true
                binding.curatorSongNextFab.isEnabled = true
            }
        }
    }

    private fun checkCategoryButtons() {              //check whether the category list is at the top or bottom
        when (currentCategoryPosition) {
            CATEGORY_ENERGY -> {
                binding.curatorSongCatPreviousMb.isEnabled = false
                binding.curatorSongCatNextMb.isEnabled = true
            }
            CATEGORY_ACOUSTIC -> {
                binding.curatorSongCatPreviousMb.isEnabled = true
                binding.curatorSongCatNextMb.isEnabled = false
            }
            else -> {
                binding.curatorSongCatPreviousMb.isEnabled = true
                binding.curatorSongCatNextMb.isEnabled = true
            }
        }
    }
}

data class CuratorCategory(
    val name: String,
    val desc: String,
    val leftText: String,
    val rightText: String
)