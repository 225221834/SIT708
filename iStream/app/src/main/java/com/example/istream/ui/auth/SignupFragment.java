package com.example.istream.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.istream.data.AppDatabase;
import com.example.istream.data.User;
import com.example.istream.databinding.FragmentSignUpBinding;

 //Fragment responsible for new user account creation.
 //Allows users to create an account by providing their full name, username, and password.

public class SignupFragment extends Fragment {

    private FragmentSignUpBinding binding;

    //Inflates the signup layout using ViewBinding.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Initializes UI components and sets up on-click listeners for the signup process.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSignUp.setOnClickListener(v -> performSignUp());
        binding.btnBackToLogin.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    //Validates input fields and creates a new user account if validation succeeds.
    private void performSignUp() {
        if (binding == null) return;

        String fullName = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
        String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

        // Check for empty input fields
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validates that passwords do match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Basic password length validation
        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform the registration on a background thread
        Context context = requireContext().getApplicationContext();
        new Thread(() -> {
            AppDatabase appDatabase = AppDatabase.getInstance(context);

            // Check if the chosen username is already taken
            if (appDatabase.appDao().checkUsernameExists(username) != null) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show());
                }
                return;
            }
            // Creates a new User object and saves it to the database
            User user = new User();
            user.setFullName(fullName);
            user.setUsername(username);
            user.setPassword(password);   // passwords stored as clear text

            appDatabase.appDao().insertUser(user);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    if (isAdded()) {
                        Navigation.findNavController(requireView()).navigateUp(); // Return to Login screen
                    }
                });
            }
        }).start();
    }
     //Clean up binding to prevent any memory leaks.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}