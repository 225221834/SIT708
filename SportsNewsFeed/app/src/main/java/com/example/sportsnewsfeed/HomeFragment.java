
package com.example.sportsnewsfeed;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private NewsAdapter latestAdapter;
    private List<NewsItem> allNews;
    private List<NewsItem> filteredNews;

    /* Inflates the layout and initializes the UI components of the home screen.
       Sets up the featured matches (horizontal list), latest news (grid), and the category filter.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rview_featured);
        RecyclerView recyclerViewLatest = view.findViewById(R.id.rview_latest);
        Spinner spinnerCategory = view.findViewById(R.id.spinner_category);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLatest.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        allNews = NewsDataRepository.getAllNews();
        filteredNews = new ArrayList<>(allNews);
        
        NewsAdapter featuredAdapter = new NewsAdapter(NewsDataRepository.getFeaturedMatches(), this::onNewsClick);
        latestAdapter = new NewsAdapter(filteredNews, this::onNewsClick);

        recyclerView.setAdapter(featuredAdapter);
        recyclerViewLatest.setAdapter(latestAdapter);

        // News category filter spinner
        String[] categories = {"All", "Football", "Basketball", "Cricket", "Tennis"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = categories[position];
                filterNews(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    /*  Filters the news list based on the provided category.
        Updates the UI by notifying the latest news adapter of the data change.
        The category to filter by (e.g., "Football", "Tennis", or "All").
     */
    @SuppressLint("NotifyDataSetChanged")
    private void filterNews(String category) {
        filteredNews.clear();
        if (category.equals("All")) {
            filteredNews.addAll(allNews);
        } else {
            for (NewsItem item : allNews) {
                if (item.getCategory().equals(category)) {
                    filteredNews.add(item);
                }
            }
        }
        latestAdapter.notifyDataSetChanged();
    }


     // Callback method triggered when a news item is clicked.
     // Navigates to the detailed view of the selected news item.
    private void onNewsClick(NewsItem item) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openDetailFragment(item);
        }
    }
}