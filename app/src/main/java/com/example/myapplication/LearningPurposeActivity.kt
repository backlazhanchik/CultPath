package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culturalapp.database.DatabaseHelper

class LearningPurposeActivity : AppCompatActivity() {

    private lateinit var rgPurpose: RadioGroup
    private lateinit var btnNext: Button
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_purpose)


        rgPurpose = findViewById(R.id.rgPurpose)
        btnNext = findViewById(R.id.btnNext)


        databaseHelper = DatabaseHelper(this)


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        userId = sharedPref.getLong("USER_ID", -1)

        if (userId == -1L) {

            Toast.makeText(this, "Срок действия сеанса истек. Пожалуйста, войдите в систему еще раз.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }


        btnNext.setOnClickListener {
            saveLearningPurpose()
        }
    }

    private fun saveLearningPurpose() {
        val selectedRadioButtonId = rgPurpose.checkedRadioButtonId
        
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Пожалуйста, выберите цель обучения", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val purpose = selectedRadioButton.text.toString()


        databaseHelper.saveLearningPurpose(userId, purpose)


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("LEARNING_PURPOSE", purpose)
            apply()
        }


        val intent = Intent(this, AdditionalCommentActivity::class.java)
        startActivity(intent)
    }
}
