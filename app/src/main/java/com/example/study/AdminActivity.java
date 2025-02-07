package com.example.study;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.study.database.AppDatabase;
import com.example.study.database.User;
import com.example.study.databinding.ActivityAdminBinding;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private ExecutorService executorService;
    private AppDatabase db;
    private AdminUsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Настройка RecyclerView
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminUsersAdapter();
        binding.rvUsers.setAdapter(adapter);

        // Обработка нажатия на кнопку удаления пользователя
        adapter.setOnUserDeleteListener(user -> {
            executorService.execute(() -> {
                db.userDao().deleteUser(user);
                runOnUiThread(() -> {
                    Toast.makeText(AdminActivity.this, "Пользователь удалён", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Обновляем список пользователей
                });
            });
        });

        loadUsers();
    }

    private void loadUsers() {
        executorService.execute(() -> {
            List<User> userList = db.userDao().getAllUsers();
            runOnUiThread(() -> adapter.setUsers(userList));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
