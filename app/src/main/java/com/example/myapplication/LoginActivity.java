package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.DatabaseHelper;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private ImageButton btnLogin;
    private TextView tvRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set click listeners
        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty()) {
            etEmail.setError("Пожалуйста, введите свой адрес электронной почты");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Пожалуйста, введите действительный адрес электронной почты");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Пожалуйста, введите свой пароль");
            etPassword.requestFocus();
            return;
        }

        // Check user credentials
        if (databaseHelper.checkUser(email, password)) {
            Toast.makeText(this, "Вход в систему прошел успешно", Toast.LENGTH_SHORT).show();

            // Save user session
            long userId = databaseHelper.getUserIdByEmail(email);
            SharedPreferences sharedPref = getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("USER_ID", userId);
            editor.putString("USER_EMAIL", email);
            editor.apply();

            // Check if user has interests
            List<String> interests = databaseHelper.getUserInterests(userId);

            Intent intent;
            if (interests.isEmpty()) {
                // No interests - go to interests selection
                intent = new Intent(this, InterestsSelectionActivity.class);
            } else {
                // Has interests - go to main activity
                intent = new Intent(this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Неверный адрес электронной почты или пароль", Toast.LENGTH_SHORT).show();
        }
    }
}