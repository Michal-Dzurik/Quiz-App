package com.dzurikm.quizapp.views;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.dzurikm.quizapp.R;

public class ProgressBar extends android.widget.ProgressBar {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setScrollBarStyle(R.style.Base_Widget_AppCompat_ProgressBar_Horizontal);
        setMax(100);
        setProgress(0);

    }

}
