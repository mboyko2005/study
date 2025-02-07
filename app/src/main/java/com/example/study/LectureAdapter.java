package com.example.study;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.databinding.ItemLectureBinding;

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
        ItemLectureBinding binding = ItemLectureBinding.inflate(LayoutInflater.from(context), parent, false);
        return new LectureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);
        holder.binding.tvLectureTitle.setText(lecture.getTitle());
        holder.binding.tvLectureContentPreview.setText(lecture.getPreviewContent());

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
        ItemLectureBinding binding;

        public LectureViewHolder(@NonNull ItemLectureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
