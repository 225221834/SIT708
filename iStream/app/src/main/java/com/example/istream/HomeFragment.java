package com.example.istream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.istream.data.AppDatabase;
import com.example.istream.data.PlaylistItem;
import com.example.istream.databinding.FragmentHomeBinding;


 //Fragment representing the home screen where users can input YouTube URLs
 //to play videos or add them to their personal playlist.

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private int currentUserId = -1;


     //Inflates the layout for this fragment using ViewBinding.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

     //Initializes UI components, sets up click listeners, and retrieves the current user ID.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the logged-in user ID from SharedPreferences
        currentUserId = requireContext().getSharedPreferences("iStreamPrefs", 0)
                .getInt("currentUserId", -1);

        // Setup click listeners for the main actions
        binding.btnPlay.setOnClickListener(v -> playVideo());
        binding.btnAddToPlaylist.setOnClickListener(v -> addToPlaylist());
        binding.btnMyPlaylist.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_playlist)
        );
        binding.btnLogout.setOnClickListener(v -> logout());
    }

     //Validates the input URL and navigates to the PlayerFragment if valid.

    private void playVideo() {
        String url = binding.etYoutubeUrl.getText().toString().trim();
        if (url.isEmpty() || !YoutubeUtils.isValidYoutubeUrl(url)) {
            Toast.makeText(requireContext(), "Please enter a valid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("youtube_url", url);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_home_to_player, bundle);
    }

     //Validates the URL, checks for duplicates, and saves the video to the database.
    private void addToPlaylist() {
        String url = binding.etYoutubeUrl.getText().toString().trim();

        if (url.isEmpty() || !YoutubeUtils.isValidYoutubeUrl(url)) {
            Toast.makeText(requireContext(), "Enter a valid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserId == -1) return;

        // Run database operations on a background thread
        new Thread(() -> {
            AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
            
            // Check if the URL is already in the user's playlist
            if (appDatabase.appDao().checkUrlExists(currentUserId, url) != null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "URL already exists in playlist!", Toast.LENGTH_SHORT).show());
                return;
            }
            //
            PlaylistItem item = new PlaylistItem();
            item.setUserId(currentUserId);
            item.setYoutubeUrl(url);

            appDatabase.appDao().insertPlaylistItem(item);

            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Added to playlist!", Toast.LENGTH_SHORT).show());
        }).start();
    }

     //Logs the user out by clearing session data and navigating back to the login screen.
    private void logout() {
        // Clear session data from SharedPreferences
        requireContext().getSharedPreferences("iStreamPrefs", 0).edit().clear().apply();
        
        // Use NavOptions to clear the backstack when returning to login
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build();
        
        Navigation.findNavController(requireView()).navigate(R.id.loginFragment, null, navOptions);
    }

     //Cleans up binding to prevent any memory leaks.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}