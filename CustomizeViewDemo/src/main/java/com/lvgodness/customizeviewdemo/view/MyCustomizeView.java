package com.lvgodness.customizeviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LYZ on 2017/10/25 0025.
 */

public class MyCustomizeView extends View {
    private Paint paint = new Paint();

    public MyCustomizeView(Context context) {
        super(context);
    }

    public MyCustomizeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        canvas.drawCircle(300, 600, 200, paint);
        canvas.drawCircle(800, 600, 200, paint);

    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

}
