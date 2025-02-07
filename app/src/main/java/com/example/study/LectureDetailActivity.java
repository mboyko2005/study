package com.example.study;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.databinding.ActivityLectureDetailBinding;

public class LectureDetailActivity extends AppCompatActivity {

    private ActivityLectureDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация View Binding
        binding = ActivityLectureDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("lectureTitle");
        String content = getIntent().getStringExtra("lectureContent");

        binding.tvLectureDetailTitle.setText(title);
        binding.tvLectureDetailContent.setText(content);
    }
}
