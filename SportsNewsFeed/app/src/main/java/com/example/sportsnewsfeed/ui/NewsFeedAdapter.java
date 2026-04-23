package com.example.sportsnewsfeed.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.NewsItem;
import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {

    private final List<NewsItem> newsList;
    private final OnNewsClickListener listener;

    public interface OnNewsClickListener {
        void onClick(NewsItem item);
    }
    //
    public NewsFeedAdapter(List<NewsItem> newsList, OnNewsClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = newsList.get(position);
        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());

        // Loading images from drawable for the news items.
        int resId = holder.itemView.getContext().getResources().getIdentifier(item.getImageUrl(), "drawable", holder.itemView.getContext().getPackageName());
        if (resId != 0) holder.image.setImageResource(resId);

        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            category = itemView.findViewById(R.id.txt_category);
        }
    }
}