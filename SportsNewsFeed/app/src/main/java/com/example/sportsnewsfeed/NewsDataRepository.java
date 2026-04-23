package com.example.sportsnewsfeed;

import com.example.sportsnewsfeed.NewsItem;
import java.util.ArrayList;
import java.util.List;

public class NewsDataRepository {
    public static List<NewsItem> getAllNews() {
        List<NewsItem> newsList = new ArrayList<>();

        // Featured news matches (top stories)
        newsList.add(new NewsItem("1", "Real Madrid vs FC Barcelona",
                "El Manaro Rsiolo scores twice, ends in 2-2 draw",
                "football", "Football", true));

        newsList.add(new NewsItem("2", "NBA Finals: Lakers take the lead",
                "Murray Alebrent scores 35 points",
                "basketball", "Basketball", true));

        newsList.add(new NewsItem("3", "Australia wins cricket World Cup",
                "David Sharma Greg lifts the trophy",
                "cricket", "Cricket", true));

        // Latest News (bottom stories)
        newsList.add(new NewsItem("4", "PSG wins Premier League",
                "Gundrelley strikes best on final day",
                "football", "Football", false));

        newsList.add(new NewsItem("5", "James Lebron breaks 3-point record twice",
                "Silver State Warriors rumble to shine",
                "basketball", "Basketball", false));

        newsList.add(new NewsItem("6", "Bob Kreynelle scores last century's in IPL2026",
                "CLL star shines again",
                "cricket", "Cricket", false));

        newsList.add(new NewsItem("7", "Manchester dominates UEFA Champions League",
                "Mbappe hat-trick seals last day victory",
                "football", "Football", false));

        newsList.add(new NewsItem("8", "Serena Williams making a comeback?",
                "Tennis legend hints at returning back",
                "tennis", "Tennis", false));

        return newsList;
    }
    // Featured news matches (top stories)
    public static List<NewsItem> getFeaturedMatches() {
        // Get all news items from the repository and filter by featured items
        List<NewsItem> featured = new ArrayList<>();
        for (NewsItem item : getAllNews()) {
            if (item.isFeatured()) featured.add(item);
        }
        return featured;
    }
    // Latest news (bottom stories)
    public static List<NewsItem> getRelatedStories(String category) {
        // Get all news items from the repository and filter by category and not featured items
        List<NewsItem> related = new ArrayList<>();
        for (NewsItem item : getAllNews()) {
            if (item.getCategory().equals(category) && !item.isFeatured()) {
                related.add(item);
            }
        }
        return related;
    }
}