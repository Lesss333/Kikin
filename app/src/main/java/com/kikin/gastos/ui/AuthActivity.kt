package com.kikin.gastos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kikin.gastos.R
import com.kikin.gastos.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.authPager.adapter = AuthPagerAdapter(this)
        TabLayoutMediator(binding.authTabs, binding.authPager) { tab, position ->
            tab.text = getString(if (position == 0) R.string.login else R.string.register)
        }.attach()
    }
}
