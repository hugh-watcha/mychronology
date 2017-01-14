package com.songsingasong.mychronology.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jaewoosong on 2015. 12. 4..
 */
public class STextView extends TextView {
    public STextView(Context context) {
        super(context);
        initFont(context);
    }

    public STextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    public STextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont(context);
    }

    private void initFont(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NanumGothic.ttf"));
    }
}
