package com.dzurikm.quizapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.dzurikm.quizapp.R;

public class HeadingView extends androidx.appcompat.widget.AppCompatTextView {
    public HeadingView(Context context, AttributeSet attrs) {
        super(context,attrs);

        String heading = "Heading";
        int color;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HeadingView,
                0, 0);

        try {
            heading = a.getString(R.styleable.HeadingView_android_text);
            color = a.getInt(R.styleable.HeadingView_android_textColor,R.attr.colorSecondary);

            setText(heading);
            setTextColor(color);

        } finally {
            a.recycle();
        }


    }
}
