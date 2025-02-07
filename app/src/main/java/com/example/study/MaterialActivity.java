package com.example.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.study.databinding.ActivityMaterialBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MaterialActivity extends AppCompatActivity {

    private ActivityMaterialBinding binding;
    private String courseTitle;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация View Binding
        binding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        courseTitle = intent.getStringExtra("courseTitle");
        userId = intent.getIntExtra("userId", -1);

        if (courseTitle == null || userId == -1) {
            finish();
            return;
        }

        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;

        MaterialPagerAdapter adapter = new MaterialPagerAdapter(this, courseTitle);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Лекции");
            } else if (position == 1) {
                tab.setText("Тесты");
            }
        }).attach();
    }

    public void updateCourseProgress(String courseTitle, int progress) {
        Intent intent = new Intent();
        intent.putExtra("courseTitle", courseTitle);
        intent.putExtra("progress", progress);
        setResult(RESULT_OK, intent);
    }
}
