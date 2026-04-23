package com.example.istream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.istream.data.AppDatabase;
import com.example.istream.data.PlaylistItem;
import com.example.istream.databinding.FragmentPlaylistBinding;

 //Fragment that displays a list of YouTube videos saved by the current user.
 //Provides functionality to play or delete items from the list.
public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnItemClickListener {
    private FragmentPlaylistBinding binding;
    private PlaylistAdapter adapter;
    private int currentUserId = -1;

    //Inflates the layout for the playlist using ViewBinding.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

     //Sets up the RecyclerView and starts observing the playlist data.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve current user ID to filter the playlist
        currentUserId = requireContext().getSharedPreferences("iStreamPrefs", 0)
                .getInt("currentUserId", -1);

        setupRecyclerView();
        observePlaylist();
    }

     //Configures the RecyclerView with a LinearLayoutManager and the custom adapter.
    private void setupRecyclerView() {
        adapter = new PlaylistAdapter(this);
        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPlaylist.setAdapter(adapter);
    }
     //Observes the LiveData from the Room database to automatically update the list.

    private void observePlaylist() {
        if (currentUserId == -1) return;

        AppDatabase.getInstance(requireContext()).appDao().getPlaylistForUser(currentUserId)
                .observe(getViewLifecycleOwner(), items -> {
                    if (items != null) {
                        adapter.setItems(items);
                        // Toggle visibility of empty state message
                        binding.tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                });
    }

    //Interface callback for when a playlist item is clicked. Navigates to the video player.
    @Override
    public void onItemClick(PlaylistItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("youtube_url", item.getYoutubeUrl());
        Navigation.findNavController(requireView()).navigate(R.id.action_playlist_to_player, bundle);
    }

    //Interface callback for the delete button click. Removes the item from the database.
    @Override
    public void onDeleteClick(PlaylistItem item) {
        new Thread(() -> {
            AppDatabase.getInstance(requireContext()).appDao().deletePlaylistItem(item.getId(), currentUserId);
            requireActivity().runOnUiThread(() -> 
                Toast.makeText(requireContext(), "Removed from playlist", Toast.LENGTH_SHORT).show());
        }).start();
    }
     //Clean up binding to prevent memory leaks.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}