package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culturalapp.database.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: ImageButton
    private lateinit var tvRegister: TextView
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)


        databaseHelper = DatabaseHelper(this)


        btnLogin.setOnClickListener {
            loginUser()
        }


        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()


        if (email.isEmpty()) {
            etEmail.error = "Пожалуйста, введите свой адрес электронной почты"
            etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Пожалуйста, введите действительный адрес электронной почты"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Пожалуйста, введите свой пароль"
            etPassword.requestFocus()
            return
        }


        if (databaseHelper.checkUser(email, password)) {
            Toast.makeText(this, "Вход в систему прошел успешно", Toast.LENGTH_SHORT).show()
            

            val userId = databaseHelper.getUserIdByEmail(email)
            val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putLong("USER_ID", userId)
                putString("USER_EMAIL", email)
                apply()
            }
            

            val interests = databaseHelper.getUserInterests(userId)
            
            if (interests.isEmpty()) {

                val intent = Intent(this, InterestsSelectionActivity::class.java)
                startActivity(intent)
            } else {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        } else {
            Toast.makeText(this, "Неверный адрес электронной почты или пароль", Toast.LENGTH_SHORT).show()
        }
    }
}
