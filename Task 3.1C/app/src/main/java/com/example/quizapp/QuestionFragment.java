package com.example.quizapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuestionFragment extends Fragment {

    private QuizViewModel viewModel;
    private int position;
    private OptionsAdapter adapter;
    private Button submitButton;
    private boolean submitted = false;
    private int tempSelected = -1;

    public static QuestionFragment newInstance(int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        position = getArguments().getInt("position");

        TextView questionTv = view.findViewById(R.id.question_text);
        RecyclerView recyclerView = view.findViewById(R.id.options_recycler);
        submitButton = view.findViewById(R.id.submit_button);

        Questions question = viewModel.getQuestions().get(position);
        questionTv.setText(question.getText());

        //sets up recycler view for options & tells recycler to display items vertically
        //one item per row
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new OptionsAdapter(question.getOptions(), question.getCorrectIndex(), pos -> {
            if (!submitted) {
                tempSelected = pos;
                adapter.setSelected(pos);
            }
        });
        recyclerView.setAdapter(adapter);

        // Restore if already answered
        List<Integer> answers = viewModel.getUserAnswersLive().getValue();
        if (answers != null && answers.get(position) != -1) {
            submitted = true;
            tempSelected = answers.get(position);
            adapter.setSelected(tempSelected);
            adapter.setSubmitted(true);
            submitButton.setText("Next");
        }
        //Submit button for each questions
        submitButton.setOnClickListener(v -> {
            if (!submitted) {
                if (tempSelected == -1) {
                    Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                    return;
                }//submitted answer, next button to proceed to next question
                viewModel.submitAnswer(position, tempSelected);
                submitted = true;
                adapter.setSubmitted(true);
                submitButton.setText("Next");
            } else {
                // Go to next question
                if (getActivity() instanceof QuizActivity) {
                    ((QuizActivity) getActivity()).goToNextQuestion(position);
                }
            }
        });
    }
}