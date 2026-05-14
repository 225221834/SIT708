package com.example.llmchatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.llmchatbot.database.Message;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;
    private List<Message> messages = new ArrayList<>();
    private String userLetter = "U";


    // Updates the list of messages and refreshes the RecyclerView.
    // The new list of messages to display.
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


     // Sets the letter to be displayed in the user's profile avatar.
     // letter -  first letter of the user's name.

    public void setUserLetter(String letter) {
        this.userLetter = letter;
    }

    //Determines whether a message was sent or received to choose the correct layout.
    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isSentByUser) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }


     // Inflates the appropriate layout for sent or received based on the type of view
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

     // Binds the message data to the view holder at the specified position.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message, userLetter);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    // The view holder for the user sent messages.
    // Handles text, time, and the profile icon first letter of the username.

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView body, time, profileLetter;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.text_message_body);
            time = itemView.findViewById(R.id.text_message_time);
            profileLetter = itemView.findViewById(R.id.text_message_profile_letter);
        }

        void bind(Message message, String letter) {
            body.setText(message.content);
            time.setText(formatTime(message.timestamp));
            if (profileLetter != null) {
                profileLetter.setText(letter);
            }
        }
    }

     // The view holder for the AI-received messages and handles texts and time
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView body, time;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.text_message_body);
            time = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            body.setText(message.content);
            time.setText(formatTime(message.timestamp));
        }
    }


     // Formats the timestamp to be readable
    private static String formatTime(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return simpleDateFormat.format(new Date(timestamp));
    }
}