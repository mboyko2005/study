package com.example.study;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private final List<Course> courses;
    private final OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CoursesAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseTitle.setText(course.getTitle());
        holder.progressText.setText(course.getProgress() + "% complete");
        holder.progressBar.setProgress(course.getProgress());
        holder.courseBackground.setImageResource(course.getImageResId());

        holder.itemView.setOnClickListener(v -> listener.onCourseClick(course));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle, progressText;
        ProgressBar progressBar;
        ImageView courseBackground;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.tv_course_title);
            progressText = itemView.findViewById(R.id.tv_course_progress);
            progressBar = itemView.findViewById(R.id.progress_course);
            courseBackground = itemView.findViewById(R.id.iv_course_background);
        }
    }
}
