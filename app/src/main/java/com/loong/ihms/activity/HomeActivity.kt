package com.loong.ihms.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.loong.ihms.R
import com.loong.ihms.base.BaseActivity
import com.loong.ihms.databinding.ActivityHomeBinding
import com.loong.ihms.fragment.MainCuratorFragment
import com.loong.ihms.fragment.MainHomeFragment
import com.loong.ihms.fragment.MainNowPlayingFragment
import com.loong.ihms.model.Song
import com.loong.ihms.utils.ConstantDataUtil
import com.loong.ihms.utils.fromJson

private const val ID_HOME_CONTAINER_FL: Int = R.id.home_container_fl
private const val ID_FRAGMENT_HOME: Int = R.id.home_nav
private const val ID_FRAGMENT_NOW_PLAYING: Int = R.id.now_playing_nav
private const val ID_FRAGMENT_CURATOR: Int = R.id.curator_nav
private const val ID_LOGOUT: Int = R.id.logout_side

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private var currentFragment: Fragment? = null

    private val broadcastReceiver = object : BroadcastReceiver() {               //to receive data from broadcast manager
        override fun onReceive(context: Context?, intent: Intent?) {
            Handler(Looper.getMainLooper()).postDelayed({
                val songListJsonStr = intent?.getStringExtra(ConstantDataUtil.START_PLAYING_SONG_LIST_EXTRA) ?: ""
                val songPosition = intent?.getIntExtra(ConstantDataUtil.START_PLAYING_SONG_POSITION_EXTRA, 0) ?: 0

                if (songListJsonStr.isNotEmpty()) {                             //receive songlist and track position and send to now playing fragment
                    binding.homeBottomNavView.selectedItemId = ID_FRAGMENT_NOW_PLAYING
                    showFragment(ID_FRAGMENT_NOW_PLAYING.toString())

                    val songList = Gson().fromJson<ArrayList<Song>>(songListJsonStr)
                    val fragment = fragmentManager.findFragmentByTag(ID_FRAGMENT_NOW_PLAYING.toString())

                    if (fragment is MainNowPlayingFragment) {
                        fragment.startPlayingSong(songList, songPosition)
                    }
                }
            }, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.homeBottomNavView.setOnNavigationItemSelectedListener(this)
        binding.homeNavView.setNavigationItemSelectedListener(this)
        binding.homeToolbar.setNavigationOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        setupFragments()

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(
                broadcastReceiver,
                IntentFilter(ConstantDataUtil.START_PLAYING_INTENT)
            )
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ID_FRAGMENT_HOME -> {
                showFragment(ID_FRAGMENT_HOME.toString())
                return true
            }

            ID_FRAGMENT_NOW_PLAYING -> {
                showFragment(ID_FRAGMENT_NOW_PLAYING.toString())
                return true
            }

            ID_FRAGMENT_CURATOR -> {
                showFragment(ID_FRAGMENT_CURATOR.toString())
                return true
            }

            ID_LOGOUT -> {
                val intent = Intent(this, IpLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                return true
            }
        }

        return false
    }

    private fun setupFragments() {
        val homeFragment = MainHomeFragment()
        val nowPlayingFragment = MainNowPlayingFragment()
        val curatorFragment = MainCuratorFragment()

        fragmentManager
            .beginTransaction()
            .add(ID_HOME_CONTAINER_FL, homeFragment, ID_FRAGMENT_HOME.toString())
            .hide(homeFragment)
            .commit()

        fragmentManager
            .beginTransaction()
            .add(ID_HOME_CONTAINER_FL, nowPlayingFragment, ID_FRAGMENT_NOW_PLAYING.toString())
            .hide(nowPlayingFragment)
            .commit()

        fragmentManager
            .beginTransaction()
            .add(ID_HOME_CONTAINER_FL, curatorFragment, ID_FRAGMENT_CURATOR.toString())
            .hide(curatorFragment)
            .commit()

        fragmentManager.executePendingTransactions()
        showFragment(ID_FRAGMENT_HOME.toString())
    }

    private fun showFragment(tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag)

        if (fragment != null) {
            replaceFragment(fragment)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (currentFragment == null) {
            fragmentManager
                .beginTransaction()
                .show(fragment)
                .commit()
        } else if (currentFragment != null && fragment != currentFragment) {
            fragmentManager
                .beginTransaction()
                .hide(currentFragment!!)
                .show(fragment)
                .commit()
        }

        fragmentManager.executePendingTransactions()
        currentFragment = fragment
    }
}