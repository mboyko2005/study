package com.example.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.database.AppDatabase;
import com.example.study.database.CourseProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CoursesActivity extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private CoursesAdapter coursesAdapter;
    private List<Course> courses;
    private int userId;
    private ExecutorService executorService;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        executorService = Executors.newSingleThreadExecutor();
        db = AppDatabase.getInstance(this);

        SharedPreferences userPrefs = getSharedPreferences("ActiveUser", MODE_PRIVATE);
        userId = userPrefs.getInt("userId", -1);
        if (userId == -1) {
            // Обработка случая, когда userId не найден
            finish();
            return;
        }

        coursesRecyclerView = findViewById(R.id.rv_courses);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        courses = new ArrayList<>();
        courses.add(new Course("Английский язык", R.drawable.course_image2));
        courses.add(new Course("Немецкий язык", R.drawable.course_image5));
        courses.add(new Course("Программирование", R.drawable.course_image3));
        courses.add(new Course("Французский язык", R.drawable.course_image4));
        courses.add(new Course("Математика", R.drawable.course_image1));

        loadCourseProgress();

        coursesAdapter = new CoursesAdapter(courses, course -> {
            Intent intent = new Intent(CoursesActivity.this, MaterialActivity.class);
            intent.putExtra("courseTitle", course.getTitle());
            intent.putExtra("userId", userId);
            startActivityForResult(intent, 1);
        });

        coursesRecyclerView.setAdapter(coursesAdapter);
    }

    private void loadCourseProgress() {
        executorService.execute(() -> {
            List<CourseProgress> progressList = db.courseProgressDao().getProgressForUser(userId);
            runOnUiThread(() -> {
                for (CourseProgress cp : progressList) {
                    for (Course course : courses) {
                        if (course.getTitle().equals(cp.getCourseTitle())) {
                            course.updateProgress(cp.getProgress());
                            break;
                        }
                    }
                }
                coursesAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String courseTitle = data.getStringExtra("courseTitle");
            int progress = data.getIntExtra("progress", 0);

            for (Course course : courses) {
                if (course.getTitle().equals(courseTitle)) {
                    course.updateProgress(progress);
                    saveCourseProgress(courseTitle, progress);
                    coursesAdapter.notifyItemChanged(courses.indexOf(course));
                    break;
                }
            }
        }
    }

    private void saveCourseProgress(String courseTitle, int progress) {
        executorService.execute(() -> {
            CourseProgress cp = new CourseProgress(userId, courseTitle, progress);
            db.courseProgressDao().insertOrUpdate(cp);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
