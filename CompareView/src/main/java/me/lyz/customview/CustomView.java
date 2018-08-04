package me.lyz.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LYZ on 2018/3/30 0030.
 */
public class CustomView extends AppCompatImageView {

    private Context context;

    private double progress;

//    private static final float[] GREY_SCALE = new float[]{
//            0.30F, 0.59F, 0.11F, 0, 0,
//            0.30F, 0.59F, 0.11F, 0, 0,
//            0.30F, 0.59F, 0.11F, 0, 0,
//            0, 0, 0, 1, 0,
//    };

    //灰阶滤镜矩阵-RGB权值调整
    private static final float[] GREY_SCALE = new float[]{
            0.36F, 0.56F, 0.08F, 0, 0,
//            1F, 0, 0, 0, 0,
            0.27F, 0.65F, 0.08F, 0, 0,
            0.27F, 0.65F, 0.17F, 0, 0,
            0, 0, 0, 1, 0,
    };


    private static final float[] COMMON = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0
    };
    private ColorMatrixColorFilter filterCommon = new ColorMatrixColorFilter(COMMON);
    private ColorMatrixColorFilter filterGrey = new ColorMatrixColorFilter(GREY_SCALE);

    //    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int measuredHeight, measureWidth;
    private float scale;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        try {
            int pro = typedArray.getInt(R.styleable.CustomView_progress, 0);
            if (pro <= 0 || pro > 100)
                progress = 0;
            else progress = pro / 100f;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (getDrawable() != null) {

            int drawableRealWidth = (int) (getDrawable().getIntrinsicWidth() * scale);
            int drawableRealHeight = (int) (getDrawable().getIntrinsicHeight() * scale);

            int translateX = (measureWidth - drawableRealWidth) / 2;
            int translateY = (measuredHeight - drawableRealHeight) / 2;

            //绘制左半边正常部分
            canvas.save();
            getDrawable().setColorFilter(filterCommon);
            canvas.clipRect((float) (getScrollX() + getPaddingLeft()),
                    (float) (getScrollY() + getPaddingTop()),
                    (float) (getScrollX() + getPaddingLeft() + measureWidth * progress),
                    (float) (getScrollY() + measuredHeight - getPaddingBottom()));
            canvas.translate(translateX, translateY);
            canvas.scale(scale, scale);
            getDrawable().draw(canvas);
            canvas.restore();


            //绘制右半边滤镜处理后部分
            canvas.save();
            getDrawable().setColorFilter(filterGrey);
            canvas.clipRect((float) (getScrollX() + getPaddingLeft() + measureWidth * progress),
                    (float) (getScrollY() + getPaddingTop()),
                    (float) (getScrollX() + +measureWidth - getPaddingRight()),
                    (float) (getScrollY() + measuredHeight - getPaddingBottom()));
            canvas.translate(translateX, translateY);
            canvas.scale(scale, scale);
            getDrawable().draw(canvas);
            canvas.restore();


            //绘制间隔线
            if (progress == 0 || progress == 1)
                return;
            canvas.save();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(4);

            canvas.drawLine((float) (getScrollX() + getPaddingLeft() + measureWidth * progress),
                    (float) (getScrollY() + getPaddingTop()),
                    (float) (getScrollX() + getPaddingLeft() + measureWidth * progress + 4),
                    (float) (getScrollY() + measuredHeight - getPaddingBottom()), paint);

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);

            canvas.drawLine((float) (getScrollX() + getPaddingLeft() + measureWidth * progress - 2),
                    (float) (getScrollY() + getPaddingTop()),
                    (float) (getScrollX() + getPaddingLeft() + measureWidth * progress),
                    (float) (getScrollY() + measuredHeight - getPaddingBottom()), paint);

            canvas.drawLine((float) (getScrollX() + getPaddingLeft() + measureWidth * progress + 4),
                    (float) (getScrollY() + getPaddingTop()),
                    (float) (getScrollX() + getPaddingLeft() + measureWidth * progress + 6),
                    (float) (getScrollY() + measuredHeight - getPaddingBottom()), paint);
            canvas.restore();

        } else
            super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            Toast.makeText(context, event.getX() + "DOWN", Toast.LENGTH_SHORT).show();
            progress = event.getX() * 1.0f / measureWidth;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            Toast.makeText(context, event.getX() + "MOVE", Toast.LENGTH_SHORT).show();
            progress = (event.getX()) * 1.0f / measureWidth;
        }
        return true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = getMeasuredHeight();
        measureWidth = getMeasuredWidth();
        scale = Math.min(measuredHeight * 1.0f / getDrawable().getIntrinsicHeight()
                , measureWidth * 1.0f / getDrawable().getIntrinsicWidth());


    }
}
