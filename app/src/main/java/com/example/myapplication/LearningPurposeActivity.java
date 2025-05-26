package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.DatabaseHelper;

public class LearningPurposeActivity extends AppCompatActivity {

    private RadioGroup rgPurpose;
    private Button btnNext;
    private DatabaseHelper databaseHelper;
    private long userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_purpose);

        // Initialize views
        rgPurpose = findViewById(R.id.rgPurpose);
        btnNext = findViewById(R.id.btnNext);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Get user ID from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getLong("USER_ID", -1);

        if (userId == -1L) {
            Toast.makeText(this,
                    "Срок действия сеанса истек. Пожалуйста, войдите в систему еще раз.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set click listener for next button
        btnNext.setOnClickListener(v -> saveLearningPurpose());
    }

    private void saveLearningPurpose() {
        int selectedRadioButtonId = rgPurpose.getCheckedRadioButtonId();

        if (selectedRadioButtonId == -1) {
            Toast.makeText(this,
                    "Пожалуйста, выберите цель обучения",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String purpose = selectedRadioButton.getText().toString();

        // Save purpose to database
        databaseHelper.saveLearningPurpose(userId, purpose);

        // Save to shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("LEARNING_PURPOSE", purpose);
        editor.apply();

        // Proceed to next activity
        Intent intent = new Intent(this, AdditionalCommentActivity.class);
        startActivity(intent);
    }
}