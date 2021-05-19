package com.loong.ihms.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.loong.ihms.R
import com.loong.ihms.base.BaseActivity
import com.loong.ihms.databinding.ActivityIpLoginBinding
import com.loong.ihms.utils.ConstantDataUtil

class IpLoginActivity : BaseActivity() {
    private lateinit var binding: ActivityIpLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ip_login)

        // For testing, can change to newest url
        binding.ipPortEditText.setText("192.168.1.110")
    }

    fun goToLogin(view: View) {
        var ipStr = binding.ipPortEditText.text.toString()

        if (ipStr.isNotEmpty()) { //automatically add "http://" if user didnt enter
            if (!ipStr.contains("http")) {
                ipStr = "http://${ipStr}"
            }

            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(ConstantDataUtil.IP_LOGIN_PARAMS, ipStr)
            startActivity(intent)
        }
    }
}