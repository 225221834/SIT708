package com.example.sportsnewsfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* RecyclerView Adapter for displaying the list of news item objects.
   Used in HomeFragment and BookmarksFragment to show news lists or grids.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsItem> newsItems;
    private final OnItemClickListener listener;

    //Interface definition for a callback to be invoked when a news item is clicked.
    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    /* Constructs a new NewsAdapter.
       The newsItem lists the news items to display.
       The listener will handle click events on the items.
     */
    public NewsAdapter(List<NewsItem> newsItems, OnItemClickListener listener) {
        this.newsItems = newsItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    // Create a new view holder for each item in the list of news items and inflate the news_item_card layout file
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_card, parent, false);
        return new NewsViewHolder(view);
    }
    /* Binds the data from a NewsItem to the provided NewsViewHolder.
       The ViewHolder is to bind data to view UI items
       The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    // Return the number of items in the list of news items
    public int getItemCount() {
        return newsItems.size();
    }
    /*
       ViewHolder class for individual news item views.
       Manages the binding of news data to the view's UI elements.
     */
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;
        private final TextView textViewCategory;

        // Initialize the views in the view holder with their corresponding IDs in the news_item_card layout file
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.news_image);
            textView = itemView.findViewById(R.id.news_title);
            textViewCategory = itemView.findViewById(R.id.txt_category);
        }
        /* Binds news item data to the view components.
           Also sets up the click listener for the item.
         */
        public void bind(final NewsItem item, final OnItemClickListener listener) {
            textView.setText(item.getTitle());
            textViewCategory.setText(item.getCategory());
            // Set the image resource for the news item based on its image URL
            int resId = itemView.getContext().getResources().getIdentifier(item.getImageUrl(), 
                    "drawable", itemView.getContext().getPackageName());
            // If the image resource is found, set it as the image for the ImageView in the view holder
            if (resId != 0) {
                imageView.setImageResource(resId);
            }
            // Set click listener for the item view in the view holder
            // When the item is clicked, the listener's onItemClick method is called with the clicked item as a parameter
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}