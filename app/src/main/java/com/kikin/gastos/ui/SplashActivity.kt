package com.kikin.gastos.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kikin.gastos.R
import com.kikin.gastos.util.SessionManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val sessionManager = SessionManager(this)
            val nextActivity = if (sessionManager.isLoggedIn()) {
                MainActivity::class.java
            } else {
                AuthActivity::class.java
            }
            startActivity(Intent(this, nextActivity))
            finish()
        }, SPLASH_DELAY_MS)
    }

    companion object {
        private const val SPLASH_DELAY_MS = 5000L
    }
}
