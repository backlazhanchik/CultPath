package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private ImageButton btnRegister;
    private TextView tvLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (firstName.isEmpty()) {
            etFirstName.setError("Пожалуйста, введите свое имя");
            etFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("Пожалуйста, введите вашу фамилию");
            etLastName.requestFocus();
            return;
        }

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
            etPassword.setError("Пожалуйста, введите пароль");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Длина пароля должна составлять не менее 6 символов");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Пожалуйста, подтвердите свой пароль");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Пароли не совпадают");
            etConfirmPassword.requestFocus();
            return;
        }

        long userId = databaseHelper.addUser(firstName, lastName, email, password);

        if (userId != -1L) {
            Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();

            getSharedPreferences("CulturalAppPrefs", MODE_PRIVATE)
                    .edit()
                    .putLong("USER_ID", userId)
                    .putString("USER_EMAIL", email)
                    .apply();

            Intent intent = new Intent(this, InterestsSelectionActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this,
                    "Не удалось зарегистрироваться. Возможно, адрес электронной почты уже используется.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}