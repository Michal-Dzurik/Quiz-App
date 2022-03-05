package com.dzurikm.quizapp.views;

import android.content.Context;
import android.util.AttributeSet;

import com.dzurikm.quizapp.R;

public class PreviousImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    public PreviousImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);


        setBackgroundResource(R.drawable.navigation_button_background);
        setImageResource(R.drawable.ic_baseline_arrow_back);



    }

}
