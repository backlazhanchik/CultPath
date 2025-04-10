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

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: ImageButton
    private lateinit var tvLogin: TextView
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        databaseHelper = DatabaseHelper(this)

        btnRegister.setOnClickListener {
            registerUser()
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (firstName.isEmpty()) {
            etFirstName.error = "Пожалуйста, введите свое имя"
            etFirstName.requestFocus()
            return
        }

        if (lastName.isEmpty()) {
            etLastName.error = "Пожалуйста, введите вашу фамилию"
            etLastName.requestFocus()
            return
        }

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
            etPassword.error = "Пожалуйста, введите пароль"
            etPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "Длина пароля должна составлять не менее 6 символов"
            etPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Пожалуйста, подтвердите свой пароль"
            etConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Пароли не совпадают"
            etConfirmPassword.requestFocus()
            return
        }

        val userId = databaseHelper.addUser(firstName, lastName, email, password)
        
        if (userId != -1L) {
            Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
            
            val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putLong("USER_ID", userId)
                putString("USER_EMAIL", email)
                apply()
            }
            
            val intent = Intent(this, InterestsSelectionActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Не удалось зарегистрироваться. Возможно, адрес электронной почты уже используется.", Toast.LENGTH_SHORT).show()
        }
    }
}
