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

import com.example.istream.R;
import com.example.istream.data.AppDatabase;
import com.example.istream.data.User;
import com.example.istream.databinding.FragmentLoginBinding;

 //Fragment responsible for user authentication.
 //Allows existing users to log in using their username and password.

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    //Inflates the login layout using ViewBinding.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    //Sets up click listeners for the Login and Sign Up buttons.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> performLogin());
        binding.btnSignUp.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_login_to_signUp)
        );
    }
    //Validates credentials against the database and navigates to Home on success.
    private void performLogin() {
        if (binding == null) return;

        String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        // Basic validation for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate user on a background thread
        Context context = requireContext().getApplicationContext();
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            User user = db.appDao().login(username, password);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (user != null) {
                        // Save the current user's ID to maintain the session
                        context.getSharedPreferences("iStreamPrefs", 0)
                                .edit().putInt("currentUserId", user.getId()).apply();

                        // Navigate to the home fragment
                        if (isAdded()) {
                            Navigation.findNavController(requireView())
                                    .navigate(R.id.action_login_to_home);
                        }
                    } else {
                        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

     //Clean up binding to prevent memory leaks.

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}