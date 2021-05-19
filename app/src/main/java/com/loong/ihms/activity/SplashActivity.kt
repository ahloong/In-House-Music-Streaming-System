package com.loong.ihms.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.loong.ihms.R
import com.loong.ihms.base.BaseActivity
import com.loong.ihms.databinding.ActivitySplashBinding
import com.loong.ihms.utils.UserRelatedUtil

class SplashActivity : BaseActivity() {                     //transition while login
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        val intent = Intent(this, IpLoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}