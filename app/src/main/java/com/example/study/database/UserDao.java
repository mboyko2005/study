package com.example.study.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :hashedPassword LIMIT 1")
    User authenticateUser(String email, String hashedPassword);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    // Получение списка всех пользователей
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    // Удаление пользователя из базы данных
    @Delete
    void deleteUser(User user);
}
