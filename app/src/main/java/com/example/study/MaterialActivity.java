package com.example.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MaterialActivity extends AppCompatActivity {

    private String courseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        courseTitle = getIntent().getStringExtra("courseTitle");

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

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
