package com.example.istream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.istream.databinding.FragmentPlayerBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

//Fragment that handles YouTube video playback
public class PlayerFragment extends Fragment {

    private FragmentPlayerBinding binding;
    private String youtubeUrl;

    //Inflates the layout for the player using ViewBinding.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Initializes the YouTube player view and handles the video loading logic.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the YouTube URL from the fragment arguments
        if (getArguments() != null) {
            youtubeUrl = getArguments().getString("youtube_url");
        }

        // Add the YouTubePlayerView as a lifecycle observer to manage its state automatically
        getLifecycle().addObserver(binding.youtubePlayerView);

        // Set up the listener to handle the video playback once the player is ready
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                // Extract the video ID from the URL and load the video
                String videoId = YoutubeUtils.extractVideoId(youtubeUrl);
                if (!videoId.isEmpty()) {
                    youtubePlayer.loadVideo(videoId, 0);
                }
            }
        });
    }
    //Clean up binding to prevent memory leaks.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}