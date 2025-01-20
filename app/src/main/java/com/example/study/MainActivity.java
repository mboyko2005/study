package com.example.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.database.AppDatabase;
import com.example.study.database.User;

public class MainActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = findViewById(R.id.et_email);
        passwordField = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        registerLink = findViewById(R.id.tv_register);

        AppDatabase db = AppDatabase.getInstance(this);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User user = db.userDao().authenticateUser(email, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show();

                        // Сохраняем текущего пользователя
                        SharedPreferences userPrefs = getSharedPreferences("ActiveUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPrefs.edit();
                        editor.putString("userId", user.getEmail());
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Неверный email или пароль!", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
