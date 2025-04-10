package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private val text = "CultPathAI"
    private val delayMillis = 100L
    private val splashDelayMillis = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        textView = findViewById(R.id.splashTextView)

        animateText(text)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomProgramms::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, splashDelayMillis + text.length * delayMillis)
    }

    private fun animateText(text: String) {
        var index = 0
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (index < text.length) {
                    textView.text = textView.text.toString() + text[index]
                    index++
                    handler.postDelayed(this, delayMillis)
                }
            }
        })
    }
}