package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestsSelectionActivity extends AppCompatActivity {

    private CheckBox cbArt;
    private CheckBox cbHistory;
    private CheckBox cbScience;
    private CheckBox cbCrafts;
    private CheckBox cbFolklore;
    private Button btnNext;
    private DatabaseHelper databaseHelper;
    private long userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_selection);

        // Initialize views
        cbArt = findViewById(R.id.cbArt);
        cbHistory = findViewById(R.id.cbHistory);
        cbScience = findViewById(R.id.cbScience);
        cbCrafts = findViewById(R.id.cbCrafts);
        cbFolklore = findViewById(R.id.cbFolklore);
        btnNext = findViewById(R.id.btnNext);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Get user ID from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getLong("USER_ID", -1);

        if (userId == -1L) {
            // Session expired, redirect to login
            Toast.makeText(this, "Срок действия сеанса истек. Пожалуйста, войдите в систему еще раз.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set click listener for next button
        btnNext.setOnClickListener(v -> saveInterests());
    }

    private void saveInterests() {
        List<String> selectedInterests = new ArrayList<>();

        if (cbArt.isChecked()) selectedInterests.add("Art");
        if (cbHistory.isChecked()) selectedInterests.add("History");
        if (cbScience.isChecked()) selectedInterests.add("Scientific inventions");
        if (cbCrafts.isChecked()) selectedInterests.add("Crafts");
        if (cbFolklore.isChecked()) selectedInterests.add("Folklore");

        // Validate selection
        if (selectedInterests.size() < 1) {
            Toast.makeText(this, "Пожалуйста, выберите хотя бы 1 интересующий вас объект",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedInterests.size() > 3) {
            Toast.makeText(this, "Пожалуйста, выберите не более 3 интересующих вас вопросов",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Save interests to database
        for (String interest : selectedInterests) {
            databaseHelper.saveInterest(userId, interest);
        }

        // Save to shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("SELECTED_INTERESTS", new HashSet<>(selectedInterests));
        editor.apply();

        // Proceed to next activity
        Intent intent = new Intent(this, KnowledgeLevelActivity.class);
        startActivity(intent);
    }
}