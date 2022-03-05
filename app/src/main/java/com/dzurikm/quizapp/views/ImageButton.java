package com.dzurikm.quizapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.dzurikm.quizapp.R;

public class ImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    public ImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resource = R.drawable.ic_launcher_foreground;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImageButton,
                0, 0);

        try {
            resource = a.getResourceId(R.styleable.ImageButton_android_src,resource);

        } finally {
            a.recycle();
        }


        setBackgroundResource(R.drawable.answer_item_background);
        setImageResource(resource);



    }

}
