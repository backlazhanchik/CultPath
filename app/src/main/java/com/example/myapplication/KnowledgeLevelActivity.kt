package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culturalapp.database.DatabaseHelper

class KnowledgeLevelActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long = -1
    private lateinit var selectedInterests: Set<String>
    private val interestLevelMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge_level)


        linearLayout = findViewById(R.id.linearLayoutInterests)
        btnNext = findViewById(R.id.btnNext)


        databaseHelper = DatabaseHelper(this)


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        userId = sharedPref.getLong("USER_ID", -1)
        selectedInterests = sharedPref.getStringSet("SELECTED_INTERESTS", setOf()) ?: setOf()

        if (userId == -1L || selectedInterests.isEmpty()) {

            Toast.makeText(this, "Срок действия сеанса истек. Пожалуйста, войдите в систему еще раз.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }


        for (interest in selectedInterests) {
            addInterestLevelSelection(interest)
        }


        btnNext.setOnClickListener {
            if (validateSelections()) {
                saveKnowledgeLevels()
                val intent = Intent(this, LearningPurposeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Пожалуйста, выберите уровень знаний для всех интересующих вас вопросов", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addInterestLevelSelection(interest: String) {

        val interestTextView = TextView(this)
        interestTextView.text = interest
        interestTextView.textSize = 18f
        interestTextView.setPadding(0, 16, 0, 8)
        linearLayout.addView(interestTextView)


        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.VERTICAL
        radioGroup.tag = interest

        val levels = arrayOf("Beginner", "Intermediate", "Advanced")
        for (level in levels) {
            val radioButton = RadioButton(this)
            radioButton.text = level
            radioButton.id = View.generateViewId()
            radioGroup.addView(radioButton)
        }


        linearLayout.addView(radioGroup)


        val divider = View(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1
        )
        params.setMargins(0, 16, 0, 16)
        divider.layoutParams = params
        divider.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
        linearLayout.addView(divider)


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val selectedLevel = selectedRadioButton.text.toString()
            val interestName = group.tag as String
            interestLevelMap[interestName] = selectedLevel
        }
    }

    private fun validateSelections(): Boolean {
        return interestLevelMap.size == selectedInterests.size
    }

    private fun saveKnowledgeLevels() {
        for ((interest, level) in interestLevelMap) {
            databaseHelper.updateInterestLevel(userId, interest, level)
        }


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            for ((interest, level) in interestLevelMap) {
                putString("LEVEL_$interest", level)
            }
            apply()
        }
    }
}
