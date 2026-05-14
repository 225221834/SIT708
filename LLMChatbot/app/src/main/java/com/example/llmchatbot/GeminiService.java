package com.example.llmchatbot;

import androidx.annotation.NonNull;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Communicates to Gemini AI by sending messages
// Handles wait times for server overload from high traffic
// Fixes temporary errors
public class GeminiService {
    private final GenerativeModelFutures model;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public GeminiService(String apiKey) {
        GenerativeModel generativeModel = new GenerativeModel(
                "gemini-2.5-flash",
                apiKey
        );
        this.model = GenerativeModelFutures.from(generativeModel);
    }

     // The point for generating a response and triggers the retry mechanism starting with 3 attempts.
     // prompt - the user's input message.
     // The CompletableFuture contains the AI's response text.

    public CompletableFuture<String> generateResponse(String prompt) {
        return generateResponseWithRetry(prompt, 3);
    }


    // The internal method that handles the actual API call with built-in retry logic for 503 errors.

    private CompletableFuture<String> generateResponseWithRetry(String prompt, int retriesLeft) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                completableFuture.complete(Objects.requireNonNullElse(resultText, "No response from AI."));
            }
            //Handles failure for server overload from high traffic
            @Override
            public void onFailure(@NonNull Throwable throwable) {
                // Correctly handles server overload from high traffic instead of throwing error
                // If the error is a 503 for service unavailable, it needs to wait and retry
                if (retriesLeft > 0 && throwable.getMessage() != null && throwable.getMessage().contains("503")) {
                    executor.execute(() -> {
                        try {
                            Thread.sleep(2000); // Waits for 2 seconds before retrying again
                            generateResponseWithRetry(prompt, retriesLeft - 1)
                                    .thenAccept(completableFuture::complete)
                                    .exceptionally(ex -> {
                                        completableFuture.complete("Error: " + ex.getMessage());
                                        return null;
                                    });
                        } catch (InterruptedException e) {
                            completableFuture.complete("Error: " + throwable.getMessage());
                        }
                    });
                } else {
                    completableFuture.complete("Error: " + throwable.getMessage());
                }
            }
        }, executor);

        return completableFuture;
    }
}
