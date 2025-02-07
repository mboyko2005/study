package com.example.study;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.databinding.ItemTestBinding;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private final List<Test> tests;
    private final OnTestAnsweredListener listener;
    private final SharedPreferences sharedPreferences;
    private int correctCount = 0;
    private boolean newAccount;

    public interface OnTestAnsweredListener {
        void onTestAnswered(int correctCount, int totalTests);
    }

    public TestAdapter(Context context, int userId, List<Test> tests, OnTestAnsweredListener listener, boolean isNewAccount) {
        this.tests = tests;
        this.listener = listener;
        this.newAccount = isNewAccount;
        this.sharedPreferences = context.getSharedPreferences("TestAnswers_" + userId, Context.MODE_PRIVATE);

        if (newAccount) {
            sharedPreferences.edit().clear().apply();
            newAccount = false;
        }

        for (int i = 0; i < tests.size(); i++) {
            int savedAnswer = sharedPreferences.getInt("test_" + i, -1);
            if (savedAnswer != -1 && savedAnswer == tests.get(i).getCorrectAnswerIndex()) {
                tests.get(i).setAnsweredCorrectly(true);
                correctCount++;
            } else {
                tests.get(i).setAnsweredCorrectly(false);
            }
        }
    }

    public void setTests(List<Test> newTests) {
        this.tests.clear();
        this.tests.addAll(newTests);
        if (newAccount) {
            sharedPreferences.edit().clear().apply();
            newAccount = false;
        }
        correctCount = 0;
        for (int i = 0; i < tests.size(); i++) {
            int savedAnswer = sharedPreferences.getInt("test_" + i, -1);
            if (savedAnswer != -1 && savedAnswer == tests.get(i).getCorrectAnswerIndex()) {
                tests.get(i).setAnsweredCorrectly(true);
                correctCount++;
            } else {
                tests.get(i).setAnsweredCorrectly(false);
            }
        }
        notifyDataSetChanged();
        if (listener != null) {
            listener.onTestAnswered(correctCount, tests.size());
        }
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTestBinding binding = ItemTestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = tests.get(position);
        holder.binding.tvTestQuestion.setText(test.getQuestion());
        holder.binding.rgTestOptions.removeAllViews();

        int savedAnswer = sharedPreferences.getInt("test_" + position, -1);
        holder.binding.rgTestOptions.setOnCheckedChangeListener(null);

        for (int i = 0; i < test.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(test.getOptions()[i]);
            radioButton.setId(i);
            CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(Color.parseColor("#0098EA")));
            if (savedAnswer == i) {
                radioButton.setChecked(true);
            }
            holder.binding.rgTestOptions.addView(radioButton);
        }

        holder.binding.rgTestOptions.setOnCheckedChangeListener((group, checkedId) -> {
            int oldAnswer = sharedPreferences.getInt("test_" + position, -1);
            if (oldAnswer != checkedId) {
                boolean oldIsCorrect = (oldAnswer == test.getCorrectAnswerIndex());
                boolean newIsCorrect = (checkedId == test.getCorrectAnswerIndex());
                if (oldIsCorrect) {
                    correctCount--;
                }
                if (newIsCorrect) {
                    correctCount++;
                }
                test.setAnsweredCorrectly(newIsCorrect);
                sharedPreferences.edit().putInt("test_" + position, checkedId).apply();
                if (listener != null) {
                    listener.onTestAnswered(correctCount, tests.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        ItemTestBinding binding;

        public TestViewHolder(@NonNull ItemTestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
