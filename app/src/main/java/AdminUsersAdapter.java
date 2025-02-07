package com.example.study;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.study.database.User;
import com.example.study.databinding.ItemAdminUserBinding;
import java.util.ArrayList;
import java.util.List;

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();
    private OnUserDeleteListener onUserDeleteListener;

    // Интерфейс для обработки удаления пользователя
    public interface OnUserDeleteListener {
        void onDeleteUser(User user);
    }

    public void setOnUserDeleteListener(OnUserDeleteListener listener) {
        this.onUserDeleteListener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminUserBinding binding = ItemAdminUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.binding.tvUserEmail.setText(user.getEmail());
        holder.binding.tvUserId.setText("ID: " + user.getId());

        // Обработка нажатия на кнопку "Удалить"
        holder.binding.btnDeleteUser.setOnClickListener(v -> {
            if (onUserDeleteListener != null) {
                onUserDeleteListener.onDeleteUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ItemAdminUserBinding binding;

        public UserViewHolder(@NonNull ItemAdminUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
