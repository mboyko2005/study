package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.database.AppDatabase;
import com.example.study.database.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, confirmPasswordField;
    private Button registerButton;
    private ImageView backButton;
    private TextView goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Инициализация элементов
        emailField = findViewById(R.id.et_register_email);
        passwordField = findViewById(R.id.et_register_password);
        confirmPasswordField = findViewById(R.id.et_register_confirm_password);
        registerButton = findViewById(R.id.btn_register);
        backButton = findViewById(R.id.iv_back);
        goToLogin = findViewById(R.id.tv_go_to_login);

        AppDatabase db = AppDatabase.getInstance(this);

        // Кнопка "Назад"
        backButton.setOnClickListener(v -> finish());

        // Ссылка на экран авторизации
        goToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Обработка нажатия кнопки "Зарегистрироваться"
        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User existingUser = db.userDao().getUserByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Пользователь с таким email уже существует!", Toast.LENGTH_SHORT).show());
                } else {
                    db.userDao().insertUser(new User(email, password));
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }).start();
        });
    }
}
