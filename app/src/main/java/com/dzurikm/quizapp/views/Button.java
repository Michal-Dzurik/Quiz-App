package com.dzurikm.quizapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.dzurikm.quizapp.R;

public class Button extends androidx.appcompat.widget.AppCompatButton {
    public Button(Context context, AttributeSet attrs) {
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
        setBackgroundResource(R.drawable.menu_button_background);



    }

}
