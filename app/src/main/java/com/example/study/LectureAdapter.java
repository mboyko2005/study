package com.example.study;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {

    private final Context context;
    private final List<Lecture> lectures;

    public LectureAdapter(Context context, List<Lecture> lectures) {
        this.context = context;
        this.lectures = lectures;
    }

    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture, parent, false);
        return new LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);
        holder.title.setText(lecture.getTitle());
        holder.contentPreview.setText(lecture.getPreviewContent()); // Устанавливаем краткое содержание

        // Открытие полной лекции при нажатии
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LectureDetailActivity.class);
            intent.putExtra("lectureTitle", lecture.getTitle());
            intent.putExtra("lectureContent", lecture.getFullContent());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    static class LectureViewHolder extends RecyclerView.ViewHolder {
        TextView title, contentPreview;

        public LectureViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_lecture_title);
            contentPreview = itemView.findViewById(R.id.tv_lecture_content_preview);
        }
    }
}
