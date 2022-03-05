package com.dzurikm.quizapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.dzurikm.quizapp.R;

public class ResultActivity extends AppCompatActivity {
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String score = getIntent().getExtras().getString("score");


        result = (TextView) findViewById(R.id.result);

        result.setText(score);
    }
}