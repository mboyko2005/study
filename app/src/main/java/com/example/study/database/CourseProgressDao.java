package com.example.study.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseProgressDao {

    // Если запись с таким userId и courseTitle уже существует, заменяем её
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(CourseProgress courseProgress);

    // Получаем прогресс по курсам для конкретного пользователя
    @Query("SELECT * FROM course_progress WHERE userId = :userId")
    List<CourseProgress> getProgressForUser(int userId);
}
