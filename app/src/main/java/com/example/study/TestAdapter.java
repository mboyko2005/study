package com.example.study;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private final List<Test> tests;
    private final OnTestAnsweredListener listener;
    private final SharedPreferences sharedPreferences;

    public interface OnTestAnsweredListener {
        void onTestAnswered(int correctCount, int totalTests);
    }

    public TestAdapter(Context context, String userId, List<Test> tests, OnTestAnsweredListener listener) {
        this.tests = tests;
        this.listener = listener;
        this.sharedPreferences = context.getSharedPreferences("TestAnswers_" + userId, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = tests.get(position);
        holder.question.setText(test.getQuestion());
        holder.options.removeAllViews();

        int savedAnswer = sharedPreferences.getInt("test_" + position, -1);

        for (int i = 0; i < test.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(test.getOptions()[i]);
            radioButton.setId(i);
            CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(Color.parseColor("#0098EA")));

            if (savedAnswer == i) {
                radioButton.setChecked(true);
            }

            holder.options.addView(radioButton);
        }

        holder.options.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("test_" + position, checkedId);
            editor.apply();

            if (checkedId == test.getCorrectAnswerIndex()) {
                test.setAnsweredCorrectly(true);
            } else {
                test.setAnsweredCorrectly(false);
            }

            int correctCount = (int) tests.stream().filter(Test::isAnsweredCorrectly).count();
            listener.onTestAnswered(correctCount, tests.size());
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        RadioGroup options;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_test_question);
            options = itemView.findViewById(R.id.rg_test_options);
        }
    }
}
