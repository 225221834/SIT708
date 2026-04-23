package com.example.istream;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istream.data.PlaylistItem;
import com.example.istream.databinding.ItemPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

 //Adapter class for the Playlist RecyclerView.
 //Manages the display and user interaction of saved video items.

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<PlaylistItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    //Interface to handle click events on individual playlist items.

    public interface OnItemClickListener {

         //Called when the main item view is clicked to play the video.
        void onItemClick(PlaylistItem item);

        //Called when the delete button is clicked to remove the video.
        void onDeleteClick(PlaylistItem item);
    }
    //Constructor for the adapter.
    public PlaylistAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    //Updates the data set for the adapter and refreshes the UI.
    public void setItems(List<PlaylistItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
     //Inflates the layout for a single item in the list.
    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaylistViewHolder(binding);
    }
     //Binds the data from a PlaylistItem to the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.binding.tvVideoUrl.setText(item.getYoutubeUrl());
        
        // Pass click events to the listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }
     //Returns the total number of items in the list.
    @Override
    public int getItemCount() {
        return items.size();
    }

     //ViewHolder class that holds the view references for each playlist item.

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ItemPlaylistBinding binding;

        public PlaylistViewHolder(ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}