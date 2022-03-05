package com.dzurikm.quizapp.views;

import android.content.Context;
import android.util.AttributeSet;

import com.dzurikm.quizapp.R;

public class NextImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    public NextImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);


        setBackgroundResource(R.drawable.navigation_button_background);
        setImageResource(R.drawable.ic_baseline_arrow_forward);



    }

}
