package com.example.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.study.database.AppDatabase;
import com.example.study.database.User;
import com.example.study.databinding.ActivityMainBinding;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newSingleThreadExecutor();
        AppDatabase db = AppDatabase.getInstance(this);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Если введён админский логин/пароль
            if (email.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "Администратор авторизован!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            String hashedPassword = Utils.hashPassword(password);

            executorService.execute(() -> {
                User user = db.userDao().authenticateUser(email, hashedPassword);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show();
                        // Сохраняем идентификатор пользователя
                        SharedPreferences userPrefs = getSharedPreferences("ActiveUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPrefs.edit();
                        editor.putInt("userId", user.getId());
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Неверный email или пароль!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
