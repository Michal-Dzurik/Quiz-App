package com.dzurikm.quizapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.dzurikm.quizapp.activities.MainActivity;
import com.dzurikm.quizapp.activities.QuizActivity;
import com.dzurikm.quizapp.models.JsonToQuizData;
import com.dzurikm.quizapp.models.Quiz;

import java.util.LinkedList;

public class LoadQuiz extends AsyncTask<Void, Void, LinkedList<Quiz.Question>> {
    private Context context;

    public LoadQuiz(Context context) {
        this.context = context;
    }

    @Override
    protected LinkedList<Quiz.Question> doInBackground(Void... voids) {
        JsonToQuizData jsonToQuizData = new JsonToQuizData(context,"quiz.json");

        if (jsonToQuizData.result != 0){
            return null;
        }

        LinkedList<Quiz.Question> data = jsonToQuizData.getData();
        Log.d("Data size", String.valueOf(data.size())) ;
        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(LinkedList<Quiz.Question> data) {
        super.onPostExecute(data);

        if (data == null){
            MainActivity.showShortToast("Quizový súbor je poškodený");
        }else {
            Log.d("Data size after", String.valueOf(data.size())) ;
            QuizActivity.setQuizData(data);
        }
    }
}
