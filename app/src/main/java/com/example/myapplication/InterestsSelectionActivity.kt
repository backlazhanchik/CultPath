package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culturalapp.database.DatabaseHelper

class InterestsSelectionActivity : AppCompatActivity() {

    private lateinit var cbArt: CheckBox
    private lateinit var cbHistory: CheckBox
    private lateinit var cbScience: CheckBox
    private lateinit var cbCrafts: CheckBox
    private lateinit var cbFolklore: CheckBox
    private lateinit var btnNext: Button
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests_selection)


        cbArt = findViewById(R.id.cbArt)
        cbHistory = findViewById(R.id.cbHistory)
        cbScience = findViewById(R.id.cbScience)
        cbCrafts = findViewById(R.id.cbCrafts)
        cbFolklore = findViewById(R.id.cbFolklore)
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
            saveInterests()
        }
    }

    private fun saveInterests() {
        val selectedInterests = mutableListOf<String>()

        if (cbArt.isChecked) selectedInterests.add("Art")
        if (cbHistory.isChecked) selectedInterests.add("History")
        if (cbScience.isChecked) selectedInterests.add("Scientific inventions")
        if (cbCrafts.isChecked) selectedInterests.add("Crafts")
        if (cbFolklore.isChecked) selectedInterests.add("Folklore")


        if (selectedInterests.size < 1) {
            Toast.makeText(this, "Пожалуйста, выберите хотя бы 1 интересующий вас объект", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedInterests.size > 3) {
            Toast.makeText(this, "Пожалуйста, выберите не более 3 интересующих вас вопросов", Toast.LENGTH_SHORT).show()
            return
        }


        for (interest in selectedInterests) {
            databaseHelper.saveInterest(userId, interest)
        }


        val sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putStringSet("SELECTED_INTERESTS", selectedInterests.toSet())
            apply()
        }


        val intent = Intent(this, KnowledgeLevelActivity::class.java)
        startActivity(intent)
    }
}
