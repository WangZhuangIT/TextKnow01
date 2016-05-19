package com.lingzhuo.textknow01.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lingzhuo.textknow01.R;

/**
 * Created by Wang on 2016/5/6.
 */
public class NewsTitle extends LinearLayout {
    public NewsTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_newstitle,this);
    }
}
