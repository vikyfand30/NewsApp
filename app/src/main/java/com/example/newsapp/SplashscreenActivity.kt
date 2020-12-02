package com.example.newsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler


class SplashscreenActivity : Activity () {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashscreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}