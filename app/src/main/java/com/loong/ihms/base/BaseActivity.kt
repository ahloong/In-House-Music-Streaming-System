package com.loong.ihms.base

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.loong.ihms.R
import com.loong.ihms.model.Song
import com.loong.ihms.network.ApiRepositoryFunction
import com.loong.ihms.network.ApiResponseCallback
import com.loong.ihms.utils.ConstantDataUtil
import com.loong.ihms.utils.LoadingDialog
import com.loong.ihms.utils.UserRelatedUtil

open class BaseActivity : AppCompatActivity() {         //abstract class, to use these methods in other class
    fun setupToolbar(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = ""
        toolbar.navigationIcon?.setTint(Color.WHITE)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onStop() {
        showHideLoadingDialog(false)
        super.onStop()
    }

    override fun onDestroy() {
        showHideLoadingDialog(false)
        super.onDestroy()
    }

    fun showHideLoadingDialog(isShow: Boolean) {
        if (isShow) {
            // Dismiss first then show
            loadingDialog.dismiss()
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }

    fun getAllSongs(proceedCall: () -> Unit, failedCall: () -> Unit) {
        showHideLoadingDialog(true)

        ApiRepositoryFunction.getAllSongs(object : ApiResponseCallback<ArrayList<Song>> {
            override fun onSuccess(responseData: ArrayList<Song>) {
                val curatorSongList = UserRelatedUtil.getCuratorSongList()

                responseData.forEach { oriSongItem ->
                    val curatedSong = curatorSongList.find { it.id == oriSongItem.id }
                    val defaultValue = ConstantDataUtil.DEFAULT_CATEGORY_VALUE

                    oriSongItem.energyPoint = curatedSong?.energyPoint ?: defaultValue
                    oriSongItem.danceabilityPoint = curatedSong?.danceabilityPoint ?: defaultValue
                    oriSongItem.valancePoint = curatedSong?.valancePoint ?: defaultValue
                    oriSongItem.acousticPoint = curatedSong?.acousticPoint ?: defaultValue
                    oriSongItem.isCurated = curatedSong?.isCurated ?: false
                }

                showHideLoadingDialog(false)
                UserRelatedUtil.saveAllSongList(responseData)
                proceedCall()
            }

            override fun onFailed() {
                showHideLoadingDialog(true)
                failedCall()
            }
        })
    }
}