package com.example.study;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LectureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        TextView lectureTitle = findViewById(R.id.tv_lecture_detail_title);
        TextView lectureContent = findViewById(R.id.tv_lecture_detail_content);

        // Получаем данные из Intent
        String title = getIntent().getStringExtra("lectureTitle");
        String content = getIntent().getStringExtra("lectureContent");

        // Устанавливаем данные
        lectureTitle.setText(title);
        lectureContent.setText(content);
    }
}
