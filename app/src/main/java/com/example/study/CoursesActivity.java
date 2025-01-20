package com.example.study;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private CoursesAdapter coursesAdapter;
    private List<Course> courses;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        SharedPreferences userPrefs = getSharedPreferences("ActiveUser", MODE_PRIVATE);
        userId = userPrefs.getString("userId", null);

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
            startActivity(intent);
        });

        coursesRecyclerView.setAdapter(coursesAdapter);
    }

    private void loadCourseProgress() {
        SharedPreferences preferences = getSharedPreferences("CourseProgress_" + userId, MODE_PRIVATE);
        for (Course course : courses) {
            int progress = preferences.getInt(course.getTitle(), 0);
            course.updateProgress(progress);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String courseTitle = data.getStringExtra("courseTitle");
            int progress = data.getIntExtra("progress", 0);

            for (Course course : courses) {
                if (course.getTitle().equals(courseTitle)) {
                    course.updateProgress(progress);
                    saveCourseProgress(courseTitle, progress);
                    coursesAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void saveCourseProgress(String courseTitle, int progress) {
        SharedPreferences preferences = getSharedPreferences("CourseProgress_" + userId, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(courseTitle, progress);
        editor.apply();
    }
}
