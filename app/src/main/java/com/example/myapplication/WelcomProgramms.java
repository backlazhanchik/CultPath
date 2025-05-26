package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomProgramms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom_programm);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        Button kultPatnAIButton = findViewById(R.id.kultPatnAIButton);

        welcomeTextView.setText("Добро пожаловать!");
        descriptionTextView.setText("Откройте для себя богатство российской культуры через наш уникальный интерфейс. Зарегистрируйтесь, чтобы получить доступ к персонализированным курсам и ресурсам.");
        kultPatnAIButton.setText("Продолжить");

        kultPatnAIButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomProgramms.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}