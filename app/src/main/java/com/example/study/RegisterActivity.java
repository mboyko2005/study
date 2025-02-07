package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.database.AppDatabase;
import com.example.study.database.User;
import com.example.study.databinding.ActivityRegisterBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация View Binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newSingleThreadExecutor();

        AppDatabase db = AppDatabase.getInstance(this);

        binding.ivBack.setOnClickListener(v -> finish());

        binding.tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etRegisterEmail.getText().toString().trim();
            String password = binding.etRegisterPassword.getText().toString().trim();
            String confirmPassword = binding.etRegisterConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = Utils.hashPassword(password);

            executorService.execute(() -> {
                User existingUser = db.userDao().getUserByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Пользователь с таким email уже существует!", Toast.LENGTH_SHORT).show());
                } else {
                    db.userDao().insertUser(new User(email, hashedPassword));
                    // Сохраняем флаг нового аккаунта в SharedPreferences
                    getSharedPreferences("ActiveUser", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isNewAccount", true)
                            .apply();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
