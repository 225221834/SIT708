package com.example.sportsnewsfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sportsnewsfeed.data.BookmarkManager;
import java.util.List;

public class BookmarksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rview_bookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        BookmarkManager bookmarkManager = new BookmarkManager(requireContext());
        List<NewsItem> bookmarkedItems = bookmarkManager.getBookmarks();

        NewsAdapter adapter = new NewsAdapter(bookmarkedItems, item -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDetailFragment(item);
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}