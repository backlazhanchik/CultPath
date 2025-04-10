package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culturalapp.database.DatabaseHelper

class AdditionalCommentActivity : AppCompatActivity() {

    private lateinit var etComment: EditText
    private lateinit var btnContinue: Button
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_comment)


        etComment = findViewById(R.id.etComment)
        btnContinue = findViewById(R.id.btnContinue)


        databaseHelper = DatabaseHelper(this)


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        userId = sharedPref.getLong("USER_ID", -1)

        if (userId == -1L) {

            Toast.makeText(this, "Сессия истекла. Пожалуйста, войдите снова.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }


        btnContinue.setOnClickListener {
            saveComment()
        }
    }

    private fun saveComment() {
        val comment = etComment.text.toString().trim()
        

        databaseHelper.saveComment(userId, comment)


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("ADDITIONAL_COMMENT", comment)
            apply()
        }


        Toast.makeText(this, "Персонализация завершена!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
