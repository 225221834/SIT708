package com.example.quizapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    String[] options;
    int correctIndex;
    int selectedIndex = -1;
    boolean isSubmitted = false;
    OnOptionClickListener listener;

    public interface OnOptionClickListener {
        void onOptionClicked(int position);
    }

    public OptionsAdapter(String[] options, int correctIndex, OnOptionClickListener listener) {
        this.options = options;
        this.correctIndex = correctIndex;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelected(int index) {
        this.selectedIndex = index;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSubmitted(boolean submitted) {
        this.isSubmitted = submitted;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.optionText.setText(options[position]);

        if (isSubmitted) {
            holder.itemView.setClickable(false);

            if (position == correctIndex) {
                holder.card.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.correct_green));
                holder.optionText.setTextColor(holder.itemView.getContext().getColor(android.R.color.white));
            } else if (position == selectedIndex) {
                holder.card.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.wrong_red));
                holder.optionText.setTextColor(holder.itemView.getContext().getColor(android.R.color.white));
            } else {
                // Reset to normal surface color when not selected/wrong
                holder.card.setCardBackgroundColor(holder.itemView.getContext().getColor(android.R.color.transparent));
            }
        } else {
            holder.itemView.setClickable(true);

            if (position == selectedIndex) {
                holder.card.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.selected));
                holder.optionText.setTextColor(holder.itemView.getContext().getColor(android.R.color.white));
            } else {
                // Default state - use surface color (works in both light & dark)
                holder.card.setCardBackgroundColor(holder.itemView.getContext().getColor(android.R.color.white));
                holder.optionText.setTextColor(holder.itemView.getContext().getColor(android.R.color.black));
            }
            holder.itemView.setOnClickListener(v -> listener.onOptionClicked(position));
        }
    }

    @Override
    public int getItemCount() {
        return options.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView optionText;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.option_card);
            optionText = itemView.findViewById(R.id.option_text);
        }
    }
}