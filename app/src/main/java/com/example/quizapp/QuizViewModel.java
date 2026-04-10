package com.example.quizapp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizViewModel extends ViewModel {
    List<Questions> questions = new ArrayList<>();
    MutableLiveData<List<Integer>> userAnswersLive = new MutableLiveData<>();

    public QuizViewModel() {//array list for the questions and correct answers (index)
        questions.add(new Questions("Which gas do humans breath?",
                new String[]{"Carbon dioxide", "Nitrogen", "Oxygen", "Helium"}, 2));
        questions.add(new Questions("What is the fastest land animal?",
                new String[]{"Bear", "Cheetah", "Deer", "Leopard"}, 1));
        questions.add(new Questions("Australia has how many states?",
                new String[]{"4", "7", "8", "6","5"}, 3));
        questions.add(new Questions("What is the capital of USA?",
                new String[]{"Cairo", "London", "Canberra", "Washington DC"}, 3));
        questions.add(new Questions("What is the name of our planet?",
                new String[]{"Earth", "Mars", "Jupiter", "Saturn","Neptune"}, 0));

        //mutable (changeable) list created with all set to default values
        List<Integer> answers = new ArrayList<>(Arrays.asList(-1, -1, -1, -1, -1));
        userAnswersLive.setValue(answers);
    }

    public List<Questions> getQuestions() { return questions; }
    public LiveData<List<Integer>> getUserAnswersLive() { return userAnswersLive; }

    public void submitAnswer(int position, int answerIndex) {
        List<Integer> current = userAnswersLive.getValue();
        if (current != null) {
            current.set(position, answerIndex);
            userAnswersLive.setValue(current);
        }
    }

    public int calculateScore() {
        //gets the answers provided
        List<Integer> answers = userAnswersLive.getValue();
        if (answers == null)
            return 0;
        //calculates final score
        int score = 0;
        for (int s = 0; s < questions.size(); s++) {
            if (answers.get(s) == questions.get(s).getCorrectIndex()) score++;
        }
        return score;
    }
}