package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.R

class WelcomProgramms : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom_programm)

        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val kultPatnAIButton: Button = findViewById(R.id.kultPatnAIButton)

        welcomeTextView.text = "Добро пожаловать!"
        descriptionTextView.text = "Откройте для себя богатство российской культуры через наш уникальный интерфейс. Зарегистрируйтесь, чтобы получить доступ к персонализированным курсам и ресурсам."
        kultPatnAIButton.text = "Продолжить"

        kultPatnAIButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}