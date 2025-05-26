package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.DatabaseHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KnowledgeLevelActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button btnNext;
    private DatabaseHelper databaseHelper;
    private long userId = -1;
    private Set<String> selectedInterests;
    private final Map<String, String> interestLevelMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_level);

        // Initialize views
        linearLayout = findViewById(R.id.linearLayoutInterests);
        btnNext = findViewById(R.id.btnNext);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Get user data from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getLong("USER_ID", -1);
        selectedInterests = sharedPref.getStringSet("SELECTED_INTERESTS", new HashSet<>());

        if (userId == -1L || selectedInterests == null || selectedInterests.isEmpty()) {
            Toast.makeText(this, "Срок действия сеанса истек. Пожалуйста, войдите в систему еще раз.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Add radio groups for each interest
        for (String interest : selectedInterests) {
            addInterestLevelSelection(interest);
        }

        // Set click listener for next button
        btnNext.setOnClickListener(v -> {
            if (validateSelections()) {
                saveKnowledgeLevels();
                Intent intent = new Intent(this, LearningPurposeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this,
                        "Пожалуйста, выберите уровень знаний для всех интересующих вас вопросов",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addInterestLevelSelection(String interest) {
        // Add interest title
        TextView interestTextView = new TextView(this);
        interestTextView.setText(interest);
        interestTextView.setTextSize(18);
        interestTextView.setPadding(0, 16, 0, 8);
        linearLayout.addView(interestTextView);

        // Create radio group for levels
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioGroup.setTag(interest);

        String[] levels = {"Beginner", "Intermediate", "Advanced"};
        for (String level : levels) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(level);
            radioButton.setId(View.generateViewId());
            radioGroup.addView(radioButton);
        }

        linearLayout.addView(radioGroup);

        // Add divider
        View divider = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );
        params.setMargins(0, 16, 0, 16);
        divider.setLayoutParams(params);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        linearLayout.addView(divider);

        // Set radio group listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = group.findViewById(checkedId);
            String selectedLevel = selectedRadioButton.getText().toString();
            String interestName = (String) group.getTag();
            interestLevelMap.put(interestName, selectedLevel);
        });
    }

    private boolean validateSelections() {
        return interestLevelMap.size() == selectedInterests.size();
    }

    private void saveKnowledgeLevels() {
        for (Map.Entry<String, String> entry : interestLevelMap.entrySet()) {
            databaseHelper.updateInterestLevel(userId, entry.getKey(), entry.getValue());
        }

        // Save to shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (Map.Entry<String, String> entry : interestLevelMap.entrySet()) {
            editor.putString("LEVEL_" + entry.getKey(), entry.getValue());
        }
        editor.apply();
    }
}