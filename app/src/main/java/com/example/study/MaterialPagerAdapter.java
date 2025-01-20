package com.example.study;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MaterialPagerAdapter extends FragmentStateAdapter {

    private final String courseTitle;

    public MaterialPagerAdapter(@NonNull FragmentActivity fragmentActivity, String courseTitle) {
        super(fragmentActivity);
        this.courseTitle = courseTitle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return LecturesFragment.newInstance(courseTitle);
        } else {
            return TestsFragment.newInstance(courseTitle);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Лекции и Тесты
    }
}
