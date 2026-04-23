package com.example.istream;

 //Utility class for YouTube related operations such as URL validation and video ID extraction.
public class YoutubeUtils {

     //Checks if a given string is a valid YouTube URL.
    public static boolean isValidYoutubeUrl(String url) {
        //checks for null inputs& trims whitespace from both ends
        //returns false for blank or null strings for valid url
        if (url == null || url.trim().isEmpty()) return false;
        //converts url to lower case
        String lower = url.toLowerCase();
        //returns true for longer format or shortened format
        return lower.contains("youtube.com") || lower.contains("youtu.be");
    }

     //Extracts the 11 character IDs from a YouTube URL
     //Supports both standards  - long format (youtube.com/watch?v=ID)
     //shortened format (youtu.be/ID)

    public static String extractVideoId(String url) {
        if (url == null) return "";

        //Removes any leading or trailing spaces from the URL.
        url = url.trim();

        // Handles youtu.be/ID short format
        if (url.contains("youtu.be/")) {
            //moves to 9 index starting at "y" in the url for the ID
            int start = url.indexOf("youtu.be/") + 9;
            //searches for "?" which marks the end of the ID
            int end = url.indexOf("?", start);
            //if "?" does not exists, the ID reaches the end of url
            if (end == -1)
                end = url.length();
            //extracts the ID and returns it
            return url.substring(start, end);
        }

        // Handles youtube.com/watch?v=ID which is the long format
        if (url.contains("v=")) {
            //moves to index 2 after "v=" which is the beginning of ID
            int start = url.indexOf("v=") + 2;
            //looks for "&" after the ID
            int end = url.indexOf("&", start);
            //If no "&" exists, it goes till the end of url
            if (end == -1) end = url.length();
            return url.substring(start, Math.min(start + 11, end));
            //extracts the ID and returns it - usually 11 characters long
        }
        //returns empty string for any unknown match url
        return "";
    }
}