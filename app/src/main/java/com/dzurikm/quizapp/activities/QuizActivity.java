package com.dzurikm.quizapp.activities;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.dzurikm.quizapp.R;
import com.dzurikm.quizapp.adapters.AnswerListAdapter;
import com.dzurikm.quizapp.async.LoadQuiz;
import com.dzurikm.quizapp.models.Quiz;
import com.dzurikm.quizapp.views.NextImageButton;
import com.dzurikm.quizapp.views.PreviousImageButton;
import com.dzurikm.quizapp.views.ProgressBar;
import com.dzurikm.quizapp.views.QuestionHeadingView;

import java.util.LinkedList;

public class QuizActivity extends AppCompatActivity {
    private static ProgressBar quizProgressBar;
    private static QuestionHeadingView questionHeadingView;
    private PreviousImageButton previousImageButton;
    private NextImageButton nextImageButton;
    private static RecyclerView answerListView;
    private static AnswerListAdapter adapter;

    private static ValueAnimator answerListAnimationEnter;
    private static ValueAnimator answerListAnimationLeave;

    private static ValueAnimator questionHeadingViewAnimationEnter;
    private static ValueAnimator questionHeadingViewAnimationLeave;

    private static ValueAnimator progressBarAnimation;

    private static Handler prevAnimation;
    private static Runnable prevAnimationRunnable;

    private static final int ANIMATION_DURATION = 400;

    static Context context;

    private static Quiz quiz;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        context = getApplicationContext();

        quiz = new Quiz();

        quizProgressBar = (ProgressBar) findViewById(R.id.quizProgressBar);
        questionHeadingView = (QuestionHeadingView) findViewById(R.id.quizHeading);
        previousImageButton = (PreviousImageButton) findViewById(R.id.previousButton);
        nextImageButton = (NextImageButton) findViewById(R.id.nextButton);
        answerListView = (RecyclerView) findViewById(R.id.answerList);

        answerListView.setLayoutManager(new LinearLayoutManager(getApplication()));
        answerListView.setNestedScrollingEnabled(false);

        animationInit();

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.getQuestions() != null){
                    Quiz.Question question = quiz.getCurrentQuestion();
                    quiz.nextQuestion();
                    if (quiz.getCurrentQuestion().equals(question)){

                        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this,R.style.Theme_QuizApp_AlertDialogTheme);
                        builder.setTitle("Vážne chceš vyhodnotiť quiz?");

                        TypedValue typedValue = new TypedValue();
                        Resources.Theme theme = getTheme();
                        theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
                        @ColorInt int color = typedValue.data;

                        // Set up the buttons
                        builder.setPositiveButton("Áno", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(QuizActivity.this,ResultActivity.class);
                                i.putExtra("score",quiz.getFinalScore() + "/" + quiz.getQuestions().size());
                                QuizActivity.this.startActivity(i);
                            }
                        });
                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                        AlertDialog dialog = builder.create();
                        builder.show();


                    }
                    else {
                        activityLeaveAnimations();
                        updateUIForCurrentQuestion();

                    }
                }

            }
        });

        previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.getQuestions() != null){
                    Quiz.Question question = quiz.getCurrentQuestion();
                    quiz.previousQuestion();
                    if (!question.equals(quiz.getCurrentQuestion())){
                        activityLeaveAnimations();
                        updateUIForCurrentQuestion();
                    }

                }
            }
        });

        getQuizQuestions();


    }

    public void animationInit(){
        answerListAnimationEnter = ObjectAnimator.ofFloat(answerListView,"alpha",0f,1f);
        answerListAnimationEnter.setDuration(ANIMATION_DURATION);

        answerListAnimationLeave = ObjectAnimator.ofFloat(answerListView,"alpha",1f,0f);
        answerListAnimationLeave.setDuration(ANIMATION_DURATION);

        questionHeadingViewAnimationEnter = ObjectAnimator.ofFloat(questionHeadingView,"alpha",0f,1f);
        questionHeadingViewAnimationEnter.setDuration(ANIMATION_DURATION);

        questionHeadingViewAnimationLeave = ObjectAnimator.ofFloat(questionHeadingView,"alpha",1f,0f);
        questionHeadingViewAnimationLeave.setDuration(ANIMATION_DURATION);

    }

    public static void activityEnterAnimations(){
        answerListAnimationEnter.start();
        questionHeadingViewAnimationEnter.start();
    }

    public static void activityLeaveAnimations(){
        answerListAnimationLeave.start();
        questionHeadingViewAnimationLeave.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateUIForCurrentQuestion(){


        progressBarAnimation = ObjectAnimator.ofInt(quizProgressBar,"progress",quizProgressBar.getProgress(),quiz.getProgress());
        progressBarAnimation.setDuration(ANIMATION_DURATION * 2);
        progressBarAnimation.start();


        if (prevAnimation != null) prevAnimation.removeCallbacks(prevAnimationRunnable);

        prevAnimation = new Handler();
        prevAnimationRunnable = new Runnable() {
            public void run() {
                questionHeadingView.setText(quiz.getCurrentQuestion().getQuestion());
                quizProgressBar.setProgress(quiz.getProgress(),true);
                resetAnswerAdapter();
                setPrevAnimationToNull();
            }
        };
        prevAnimation.postDelayed(prevAnimationRunnable, ANIMATION_DURATION);


    }

    public static void setPrevAnimationToNull(){
        prevAnimation = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateUIForCurrentQuestionNoWaiting(){
        quizProgressBar.setProgress(quiz.getProgress(),true);
        questionHeadingView.setText(quiz.getCurrentQuestion().getQuestion());

        resetAnswerAdapter();

    }

    public static void resetAnswerAdapter(){
        Log.i("Adapter","reseting");
        adapter = new AnswerListAdapter(context,quiz.getCurrentQuestion().getAnswer());

        answerListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        activityEnterAnimations();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void answer(String answer){
        quiz.answer(answer);
        //resetAnswerAdapter();
    }

    public void getQuizQuestions(){

        LoadQuiz load = (LoadQuiz) new LoadQuiz(getApplication()).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setQuizData(LinkedList<Quiz.Question> questions){
        quiz.setQuestions(questions);
        quizProgressBar.setMax(quiz.getQuestions().size());
        updateUIForCurrentQuestionNoWaiting();

        Log.i("Data", questions.toString());

    }





}
