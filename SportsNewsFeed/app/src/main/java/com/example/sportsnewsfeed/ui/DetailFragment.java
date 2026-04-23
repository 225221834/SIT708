package com.example.sportsnewsfeed.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sportsnewsfeed.R;
import com.example.sportsnewsfeed.NewsItem;
import com.example.sportsnewsfeed.NewsAdapter;
import com.example.sportsnewsfeed.NewsDataRepository;
import com.example.sportsnewsfeed.data.BookmarkManager;
import java.util.List;

public class DetailFragment extends Fragment {

    private NewsItem currentNews;
    private BookmarkManager bookmarkManager; // Simple SharedPreferences based

    public static DetailFragment newInstance(NewsItem newsItem) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("news", newsItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentNews = (NewsItem) getArguments().getSerializable("news");
        }
        bookmarkManager = new BookmarkManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imageView = view.findViewById(R.id.large_image);
        TextView textView = view.findViewById(R.id.detail_title);
        TextView txtDesc = view.findViewById(R.id.detail_description);
        Button buttonBookMark = view.findViewById(R.id.btn_bookmark);
        RecyclerView recyclerViewRelated = view.findViewById(R.id.rview_related);

        // Set data
        textView.setText(currentNews.getTitle());
        txtDesc.setText(currentNews.getDescription());
        //
        int resId = getResources().getIdentifier(currentNews.getImageUrl(),
                "drawable", requireContext().getPackageName());
        if (resId != 0) imageView.setImageResource(resId);

        // Bookmark initial state
        updateBookmarkButton(buttonBookMark);

        buttonBookMark.setOnClickListener(v -> {
            bookmarkManager.toggleBookmark(currentNews);
            boolean isBookmarked = bookmarkManager.isBookmarked(currentNews.getId());
            updateBookmarkButton(buttonBookMark);
            // Show toast message based on bookmark state
            // Ternary operator for user's choice
            String message = isBookmarked ? "Bookmarked!" : "Bookmark removed";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        // Related stories adapter and recycler view
        List<NewsItem> related = NewsDataRepository.getRelatedStories(currentNews.getCategory());
        NewsAdapter relatedAdapter = new NewsAdapter(related, item -> {
            // Replace current detail with new one
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit();
        });

        recyclerViewRelated.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewRelated.setAdapter(relatedAdapter);

        return view;
    }
    //Updates bookmark button upon user bookmark choice.
    private void updateBookmarkButton(Button button) {
        if (bookmarkManager.isBookmarked(currentNews.getId())) {
            button.setText("Remove Bookmark");
        } else {
            button.setText("Bookmark");
        }
    }
}