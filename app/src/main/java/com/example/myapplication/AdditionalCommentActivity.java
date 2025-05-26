package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.DatabaseHelper;

public class AdditionalCommentActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btnContinue;
    private DatabaseHelper databaseHelper;
    private long userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_comment);

        // Initialize views
        etComment = findViewById(R.id.etComment);
        btnContinue = findViewById(R.id.btnContinue);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Get user ID from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getLong("USER_ID", -1);

        if (userId == -1L) {
            // Session expired, redirect to login
            Toast.makeText(this, "Сессия истекла. Пожалуйста, войдите снова.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set click listener for continue button
        btnContinue.setOnClickListener(v -> saveComment());
    }

    private void saveComment() {
        String comment = etComment.getText().toString().trim();

        // Save comment to database
        databaseHelper.saveComment(userId, comment);

        // Save comment to shared preferences
        SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ADDITIONAL_COMMENT", comment);
        editor.apply();

        // Show success message and redirect to main activity
        Toast.makeText(this, "Персонализация завершена!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}