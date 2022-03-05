package com.dzurikm.quizapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dzurikm.quizapp.R;

public class QuestionHeadingView extends androidx.appcompat.widget.AppCompatTextView {

    public QuestionHeadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setTextAppearance(getContext(), R.style.Theme_QuizApp_QuestionHeadingView);
    }

}
