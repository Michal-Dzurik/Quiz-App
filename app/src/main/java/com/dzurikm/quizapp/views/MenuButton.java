package com.dzurikm.quizapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dzurikm.quizapp.R;

public class MenuButton extends androidx.appcompat.widget.AppCompatButton {
    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        String text = "Button";

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MenuButton,
                0, 0);

        try {
            text = a.getString(R.styleable.MenuButton_android_text);

        } finally {
            a.recycle();
        }


        setText(text);
        setAllCaps(false);
        setBackgroundResource(R.drawable.answer_item_background);



    }

}
