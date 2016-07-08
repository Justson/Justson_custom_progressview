package com.qypt.just.just_study_app;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Justson
 * Created by Administrator on 2016/7/3.
 */
public class CircleView extends View {


    private Paint paint;
    private int defCircleColor;
    private int defRingColor;
    private int defWidth = 160;
    private int defHeight = 160;
    private RectF rectF = new RectF();
    private float rx;
    private float ry;
    private static final int bouder = 3;
    private ObjectAnimator objectAnimator;

    public CircleView(Context context) {
        super(context);
        init(context, null, 0);
    }


    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, 0);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        paint = new Paint();
        paint.setColor(defCircleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setDither(true);

        int density = (int) context.getResources().getDisplayMetrics().density;
        defWidth = defWidth * density;
        defHeight = density * defHeight;


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        int count = ta.getIndexCount();

        for (int i = 0; i < count; i++) {

            int index = ta.getIndex(i);
            switch (index) {

                case R.styleable.CircleView_circleColor:
                    defCircleColor = ta.getColor(index, Color.YELLOW);
                    break;
                case R.styleable.CircleView_ringColor:
                    defRingColor = ta.getColor(index, Color.BLUE);
                    break;

            }

        }
        if (defCircleColor == 0) {
            defCircleColor = Color.parseColor("#335541");

        }
        if (defRingColor == 0) {
            defRingColor = Color.parseColor("#545633");
        }
        ta.recycle();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectF.top = 20f;
        rectF.bottom = 20f + h - 40f;
        rectF.left = 20f;
        rectF.right = 20f + w - 40f;

        rx = w / 2;
        ry = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        this.setMeasuredDimension(widthMode == MeasureSpec.AT_MOST ? defWidth : MeasureSpec.getSize(widthMeasureSpec),
                heightMode == MeasureSpec.AT_MOST ? defHeight : MeasureSpec.getSize(heightMeasureSpec));

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        objectAnimator = ObjectAnimator.ofFloat(this,"Rotation",0,359);
        objectAnimator.setDuration(6000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        objectAnimator.cancel();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(130);
        canvas.drawArc(rectF, 0, 360, false, paint);
        paint.setColor(defCircleColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(230);
        canvas.drawCircle(getWidth() / 2, rectF.top, 20, paint);
    }
}
